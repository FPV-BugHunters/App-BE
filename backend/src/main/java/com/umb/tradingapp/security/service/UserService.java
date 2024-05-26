package com.umb.tradingapp.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.umb.tradingapp.dto.CryptoPriceDTO;
import com.umb.tradingapp.entity.CryptoEntity;
import com.umb.tradingapp.entity.CryptoQuoteEntity;
import com.umb.tradingapp.repo.CryptoQuoteRepository;
import com.umb.tradingapp.repo.CryptoRepository;
import com.umb.tradingapp.security.dto.TransactionDTO;
import com.umb.tradingapp.security.entity.TokenEntity;
import com.umb.tradingapp.security.entity.TransactionEntity;
import com.umb.tradingapp.security.entity.UserEntity;
import com.umb.tradingapp.security.entity.WatchlistEntity;
import com.umb.tradingapp.security.repo.TokenRepository;
import com.umb.tradingapp.security.repo.TransactionRepository;
import com.umb.tradingapp.security.repo.UserPortfolioRepository;
import com.umb.tradingapp.security.repo.UserRepository;
import com.umb.tradingapp.security.repo.WatchlistRepository;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserService {

    @Autowired
    CryptoQuoteRepository cryptoQuoteRepo;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    UserPortfolioRepository userPortfolioRepo;

    @Autowired
    TransactionRepository transactionRepo;

    @Autowired
    WatchlistRepository watchlistRepo;

    @Autowired
    CryptoRepository cryptoIdRepo;

    private final String BALANCE = "Balance";
    
    public Double balance(Long userId, HttpServletResponse response) {
        UserEntity entity = userRepository.getReferenceById(userId);
        return entity.getBalance();
    }

    public Boolean addBalance(Double dto, HttpServletResponse response, Long userId) {
        UserEntity entity = userRepository.getReferenceById(userId);
        entity.setBalance(entity.getBalance() + dto);
        userRepository.save(entity);
        return true;
    }

    public Boolean removeBalance(Double dto, HttpServletResponse response, Long userId) {
        UserEntity entity = userRepository.getReferenceById(userId);
        entity.setBalance(entity.getBalance() - dto);
        userRepository.save(entity);
        return true;
    }

    public Boolean checkEnoughBalance(Double dto, HttpServletResponse response, Long userId) {
        UserEntity entity = userRepository.getReferenceById(userId);
        if (entity.getBalance() - dto < 0) {
            response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
            response.addHeader(BALANCE, "Not enough balance");
            return false;
        }
        response.addHeader(BALANCE, "Enough balance");
        return true;
    }

    public Boolean checkTokenGiven(Optional<String> authentification, HttpServletResponse response) {
        if (authentification.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.addHeader("Error", "token not sent"); 
            return false;
        }
        return true;
    }

    public Boolean checkTokenExists(Optional<String> authentification, HttpServletResponse response) {
        String token = authentification.get().substring("Bearer".length()).trim();
        Optional<TokenEntity> te;
        te = tokenRepository.findByToken(token);
        if (te.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.addHeader("Error", "user does not exists");
            return false; 
        }
        return true;
    }

    public <T> Boolean checkDtoExists(T dto, HttpServletResponse response) {
        if (dto.equals(null)) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.addHeader("Error", "dto not found");
            return false;
        }
        return true;
    }

    public Long getUserId(Optional<String> authentification) {
        String token = authentification.get().substring("Bearer".length()).trim();
        Optional<TokenEntity> te = tokenRepository.findByToken(token);
        return te.get().getUser().getId();
    }

    public List<TransactionDTO> getUserTransactions(Long userId) {
        List<TransactionEntity> listEntity = transactionRepo.findByUserId(userId);
        List<TransactionDTO> listDto = new ArrayList<>();
        CryptoEntity idEntity;
        
        for (TransactionEntity e : listEntity) {
            idEntity = e.getCrypto();
            listDto.add(new TransactionDTO(
                e.getId(),
                e.getAmount(),
                e.getPricePerUnit(),
                e.getTotalPrice(),
                e.getUser().getId(),
                idEntity.getId(),
                idEntity.getName(),
                idEntity.getSymbol(),
                ((e.getType() == null) ? "null" : e.getType().toString())
            ));
        }
        return listDto;
    }

    public TransactionDTO getUserTransactionById(Long userId, Long transactionId) {
        Optional<TransactionEntity> optional = transactionRepo.findById(transactionId);
        TransactionEntity entity;
        CryptoEntity idEntity;
        TransactionDTO dto = new TransactionDTO();
        if (optional.isEmpty())
            return null;
        entity = optional.get();
        idEntity = entity.getCrypto();
        dto.setAmount(entity.getAmount()); 
        dto.setCryptoId(idEntity.getId());
        dto.setCryptoName(idEntity.getName());
        dto.setCryptoSymbol(idEntity.getSymbol());
        dto.setId(entity.getId());
        dto.setUserId(userId);
        dto.setPricePerUnit(entity.getPricePerUnit());
        dto.setTotalPrice(entity.getTotalPrice());
        dto.setType(((entity.getType() == null) ? "null" : entity.getType().toString())); 
    
        return dto;
    }

    public Boolean addToWatchlist(Long cryptoId, Long userId) {
        UserEntity userEntity = userRepository.getReferenceById(userId);
        CryptoEntity cryptoIdEntity = cryptoIdRepo.getReferenceById(cryptoId);
        WatchlistEntity entity = new WatchlistEntity();
        entity.setCrypto(cryptoIdEntity);
        entity.setUser(userEntity);
        watchlistRepo.save(entity);
        return true;
    }

    public Boolean removeFromWatchlist(Long cryptoId, Long userId) {
        WatchlistEntity e = watchlistRepo.findByUserIdAndCryptoId(userId, cryptoId).get();
        watchlistRepo.delete(e);
        return true;
    }

    public boolean inWatchlist(Long cryptoId, Long userId) {
        Optional<WatchlistEntity> entity = watchlistRepo.findByUserIdAndCryptoId(userId, cryptoId);
        if (entity.isEmpty())
            return false;
        return true;
    }

    public List<CryptoPriceDTO> getUserWatchlist(Long userId) {
        List<WatchlistEntity> entityList = watchlistRepo.findAllByUserId(userId);
        List<CryptoPriceDTO> dtoList = new ArrayList<>();
        CryptoEntity cryptoId;
        CryptoQuoteEntity cryptoQuote;

        for (WatchlistEntity e : entityList) {
            cryptoId = e.getCrypto();
            cryptoQuote = cryptoQuoteRepo.findByCryptoIdOrderByLastUpdatedDesc(cryptoId.getId()).get(0);
            dtoList.add(new CryptoPriceDTO(
                cryptoId.getId(),
                cryptoId.getName(),
                cryptoId.getSymbol(),
                cryptoId.getCmc_rank(),
                cryptoQuote.getPrice(),
                cryptoQuote.getCirculatingSupply(),
                cryptoQuote.getMarketCap(),
                cryptoQuote.getVolume24h(),
                cryptoQuote.getPercentChange1h(),
                cryptoQuote.getPercentChange24h(),
                cryptoQuote.getPercentChange7d(),
                null
            ));
        }
        return dtoList;
    }

    public CryptoPriceDTO getUserWatchlistItem(Long cryptoId, Long userId) {
        Optional<WatchlistEntity> optionalEntity = watchlistRepo.findByUserIdAndCryptoId(userId, cryptoId);
        WatchlistEntity entity;
        CryptoEntity cryptoIdEntity;
        CryptoQuoteEntity cryptoQuote;  
        CryptoPriceDTO dto;

        //TODO doplnit upozornenie ze id je neplatne
        if (optionalEntity.isEmpty())
            return null;

        entity = optionalEntity.get();
        cryptoIdEntity = entity.getCrypto();
        cryptoQuote = cryptoQuoteRepo.findByCryptoIdOrderByLastUpdatedDesc(cryptoIdEntity.getId()).get(0);
        dto = new CryptoPriceDTO(
            cryptoIdEntity.getId(),
            cryptoIdEntity.getName(),
            cryptoIdEntity.getSymbol(),
            cryptoIdEntity.getCmc_rank(),
            cryptoQuote.getPrice(),
            cryptoQuote.getCirculatingSupply(),
            cryptoQuote.getMarketCap(),
            cryptoQuote.getVolume24h(),
            cryptoQuote.getPercentChange1h(),
            cryptoQuote.getPercentChange24h(),
            cryptoQuote.getPercentChange7d(),
            null
        );
        return dto;
    }


}