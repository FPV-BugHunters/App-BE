package com.umb.tradingapp.security.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.umb.tradingapp.entity.CryptoIdEntity;
import com.umb.tradingapp.entity.CryptoQuoteEntity;
import com.umb.tradingapp.entity.CryptoRankEntity;
import com.umb.tradingapp.repo.CryptoIdRepository;
import com.umb.tradingapp.security.dto.BuyTransactionDTO;
import com.umb.tradingapp.security.dto.PortfolioDTO;
import com.umb.tradingapp.security.dto.UserPortfolioDTO;
import com.umb.tradingapp.security.entity.PortfolioEntity;
import com.umb.tradingapp.security.entity.TransactionEntity;
import com.umb.tradingapp.security.entity.UserEntity;
import com.umb.tradingapp.security.entity.UserPortfolioEntity;
import com.umb.tradingapp.security.repo.PortfolioRepository;
import com.umb.tradingapp.security.repo.TransactionRepository;
import com.umb.tradingapp.security.repo.UserPortfolioRepository;
import com.umb.tradingapp.security.repo.UserRepository;
import com.umb.tradingapp.type.TransactionType;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserPortfolioService {

    @Autowired
    private UserPortfolioRepository userPortfolioRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PortfolioRepository portfolioRepo;

    @Autowired
    private CryptoIdRepository cryptoIdRepo;

    @Autowired
    private TransactionRepository transactionRepo;

    public Boolean createUserPortfolio(HttpServletResponse response, Long userId, String name) {
        UserEntity user = userRepo.getReferenceById(userId);
        UserPortfolioEntity portfolio = new UserPortfolioEntity();

        portfolio.setUser(user);
        portfolio.setName(name);
        userPortfolioRepo.save(portfolio);        
        return true;
    }
    
    public List<UserPortfolioDTO> listUserPortfolio(HttpServletResponse response, Long userId) {
        List<UserPortfolioEntity> list = userPortfolioRepo.findByUserId(userId);
        List<UserPortfolioDTO> dtoList = new ArrayList<>();

        for (UserPortfolioEntity e : list) {
            Double totalPrice = portfolioRepo.getTotalPrice(e.getId());
            Integer itemCount = portfolioRepo.getItemCount(e.getId());
            UserPortfolioDTO dto = new UserPortfolioDTO(
                e.getId(),
                itemCount,
                totalPrice,
                e.getName()
            );
            dtoList.add(dto);
        }

        return dtoList;
    }

    public Boolean checkPortfolioExists(HttpServletResponse response, Long userId, Long userPortfolioId) {
        Optional<UserPortfolioEntity> entity = userPortfolioRepo.findById(userPortfolioId);
        if (entity.isEmpty()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.addHeader("User portfolio", "user portfolio not found");
            return false;
        }
        if (entity.get().getUser().getId() != userId) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.addHeader("User portfolio", "user doesn't own user portfolio");
            return false;
        }
        return true;

    }

    public void buyCrypto(HttpServletResponse response, Long userId, BuyTransactionDTO dto) {
        PortfolioEntity portfolioEntity;
        UserPortfolioEntity userPortfolioEntity = userPortfolioRepo.getReferenceById(dto.getUserPortfolioId());
        CryptoIdEntity cryptoEntity = cryptoIdRepo.getReferenceById(dto.getCryptoId());
        UserEntity userEntity = userRepo.getReferenceById(userId);

        if (portfolioRepo.existsByUserPortfolioIdAndCryptoId(dto.getUserPortfolioId(), dto.getCryptoId())) {
            portfolioEntity = portfolioRepo.getReferenceByUserPortfolioIdAndCryptoId(dto.getUserPortfolioId(), dto.getCryptoId());
            portfolioEntity.increaseAmount(dto.getAmount());
        } else {
            portfolioEntity = new PortfolioEntity();
            portfolioEntity.setAmount(dto.getAmount());
            portfolioEntity.setCrypto(cryptoEntity);
            portfolioEntity.setUserPortfolio(userPortfolioEntity);
        }
        Double price = cryptoEntity.getQuote().getPrice();
        Double totalPrice = price * dto.getAmount();
        createTransaction(dto.getAmount(), cryptoEntity, userEntity, price, totalPrice, TransactionType.BUY);
        portfolioRepo.save(portfolioEntity);
        userPortfolioRepo.save(userPortfolioEntity);
    }

    public boolean checkCryptoOwned(BuyTransactionDTO dto, HttpServletResponse response, Long userId) {
        if (!portfolioRepo.existsByUserPortfolioIdAndCryptoId(dto.getUserPortfolioId(), dto.getCryptoId())) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setHeader("Error", "user doesn't own given crypto");
            return false;
        }
        return true;
    }

    public Double sellCrypto(BuyTransactionDTO dto, HttpServletResponse response, Long userId) {
        PortfolioEntity entity = portfolioRepo.getReferenceByUserPortfolioIdAndCryptoId(dto.getUserPortfolioId(), dto.getCryptoId());
        CryptoIdEntity cryptoEntity = cryptoIdRepo.getReferenceById(dto.getCryptoId());
        UserEntity userEntity = userRepo.getReferenceById(userId);

        Double priceTotal;
        Double pricePerUnit;
        if (entity.getAmount() <= dto.getAmount()) {
            priceTotal = entity.getTotalPrice();
            pricePerUnit = entity.getPricePerUnit();
            portfolioRepo.delete(entity);
            createTransaction(entity.getAmount(), cryptoEntity, userEntity, pricePerUnit, priceTotal, TransactionType.SELL);
            return priceTotal;
        }
        entity.decreaseAmount(dto.getAmount());
        priceTotal = entity.getPrice(dto.getAmount());
        pricePerUnit = entity.getPricePerUnit();
        portfolioRepo.save(entity);
        createTransaction(dto.getAmount(), cryptoEntity, userEntity, pricePerUnit, priceTotal, TransactionType.SELL);
        return priceTotal;
    }

    public void createTransaction(Float amount, CryptoIdEntity cryptoEntity, UserEntity userEntity, Double pricePerUnit, Double totalPrice, TransactionType type) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAmount(amount);
        transactionEntity.setCrypto(cryptoEntity);
        transactionEntity.setUser(userEntity);
        transactionEntity.setPricePerUnit(pricePerUnit);
        transactionEntity.setTotalPrice(totalPrice);
        transactionEntity.setDateTime(LocalDateTime.now());
        transactionEntity.setType(type);

        transactionRepo.save(transactionEntity);
        
    }

    public Iterable<PortfolioDTO> listPortfolio(Long userPortfolioId) {
        List<PortfolioEntity> listEntity = portfolioRepo.findByUserPortfolioId(userPortfolioId);
        List<PortfolioDTO> listDto = new ArrayList<>();
        CryptoIdEntity idEntity;
        CryptoQuoteEntity quoteEntity;
        CryptoRankEntity rankEntity;
        for (PortfolioEntity e : listEntity) {
            idEntity = e.getCrypto();
            quoteEntity = idEntity.getQuote();
            rankEntity = idEntity.getRank();
            Double totalPrice = e.getTotalPrice();
            listDto.add(new PortfolioDTO(
                idEntity.getId(),
                rankEntity.getCmcRank(),
                idEntity.getName(),
                idEntity.getSlug(),
                totalPrice,
                quoteEntity.getPrice(),
                quoteEntity.getCirculatingSupply(),
                quoteEntity.getMarketCap(),
                quoteEntity.getVolume24h(),
                e.getAmount(),
                quoteEntity.getPercentChange1h(),
                quoteEntity.getPercentChange24h(),
                quoteEntity.getPercentChange7d()
            ));
        }
        return listDto;
    }

}
