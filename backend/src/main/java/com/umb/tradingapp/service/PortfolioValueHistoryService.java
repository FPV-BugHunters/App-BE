package com.umb.tradingapp.service;

/**
* This example uses the Apache HTTPComponents library.
*/

import java.util.Date;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.sound.sampled.Port;

import com.umb.tradingapp.dto.BalanceHistoryDTO;
import com.umb.tradingapp.dto.ListingLatestCryptoDataDTO;
import com.umb.tradingapp.dto.ListingLatestDTO;
import com.umb.tradingapp.dto.ListingLatestQuoteDTO;
import com.umb.tradingapp.dto.PortfolioValueHistoryDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.hibernate.mapping.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.umb.tradingapp.entity.BalanceHistoryEntity;
import com.umb.tradingapp.entity.CryptoEntity;
import com.umb.tradingapp.entity.CryptoQuoteEntity;
import com.umb.tradingapp.entity.PortfolioEntity;
import com.umb.tradingapp.entity.PortfolioValueHistoryEntity;
import com.umb.tradingapp.entity.UserPortfolioEntity;
import com.umb.tradingapp.repo.BalanceHistoryRepository;
import com.umb.tradingapp.repo.CryptoQuoteRepository;
import com.umb.tradingapp.repo.CryptoRepository;
import com.umb.tradingapp.repo.PortfolioRepository;
import com.umb.tradingapp.repo.PortfolioValueHistoryRepository;
import com.umb.tradingapp.repo.UserPortfolioRepository;
import com.umb.tradingapp.security.entity.UserEntity;
import com.umb.tradingapp.security.repo.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class PortfolioValueHistoryService {

    @Value("${coinmarketcapUri}")
    private String coinmarketcapUri;

    @Autowired
    private PortfolioRepository portfolioRepo;

    @Autowired
    private UserPortfolioRepository userPortfolioRepo;
    

    @Autowired
    private CryptoQuoteRepository cryptoQuoteRepo;

    @Autowired
    private PortfolioValueHistoryRepository portfolioValueHistoryRepo;
    

    @Async
    @Transactional
    public void savePortfolioValue() { 
        List<UserPortfolioEntity> userPortfolios = userPortfolioRepo.findAll();

        for (UserPortfolioEntity userPortfolio : userPortfolios) {
            System.out.println("User Portfolio: " + userPortfolio.getName() );
            List<PortfolioEntity> portfolios = userPortfolio.getPortfolios();
            Double portfolioValue = 0.0;
            for (PortfolioEntity portfolio : portfolios) {
                Float amount = portfolio.getAmount();
                CryptoQuoteEntity cryptoQuote = cryptoQuoteRepo .findByCryptoIdOrderByLastUpdatedDesc(portfolio.getCrypto().getId()).get(0);
                Double value = (Double) (amount * cryptoQuote.getPrice());
                portfolioValue += value;

            }

            PortfolioValueHistoryEntity portfolioValueHistory = new PortfolioValueHistoryEntity();
            portfolioValueHistory.setDateTime(new Date());
            portfolioValueHistory.setUserPortfolio(userPortfolio);
            portfolioValueHistory.setValue(portfolioValue);
            portfolioValueHistoryRepo.save(portfolioValueHistory);
            
            
        }
    }
    
    public List<PortfolioValueHistoryDTO> getPortfolioValueHistory(Long userId, Long userPortfolioId) {
        
        List<PortfolioValueHistoryDTO> portfolioValueHistoryDTO = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "dateTime"));
        List<PortfolioValueHistoryEntity> portfolioValueHistory = portfolioValueHistoryRepo .findAll(pageable).getContent();
        for (PortfolioValueHistoryEntity portfolio : portfolioValueHistory) {
            if (portfolio.getUserPortfolio().getId() == userPortfolioId && portfolio.getUserPortfolio().getUser().getId() == userId) {
                PortfolioValueHistoryDTO portfolioDTO = new PortfolioValueHistoryDTO();
                portfolioDTO.setId(portfolio.getId());
                portfolioDTO.setValue(portfolio.getValue());
                portfolioDTO.setDateTime(portfolio.getDateTime());
                portfolioValueHistoryDTO.add(portfolioDTO);
            }
        }
        return portfolioValueHistoryDTO;
    }
    

    public PortfolioValueHistoryDTO getPortfolioValue(Long userPortfolioId, Long userId) {
        PortfolioValueHistoryEntity portfolioValueHistory = portfolioValueHistoryRepo .findByUserPortfolioIdOrderByDateTimeDesc(userPortfolioId).get(0);
        
         if (portfolioValueHistory == null) {
            throw new NoSuchElementException("No PortfolioValue found with id " + userPortfolioId);
        }
        PortfolioValueHistoryDTO dto = new PortfolioValueHistoryDTO();
        dto.setId(portfolioValueHistory.getId());
        dto.setValue(portfolioValueHistory.getValue());
        dto.setDateTime(portfolioValueHistory.getDateTime());
        return dto;
    }
    


    @Transactional
    public void removeOldPortfolioValues() {
        
        List<UserPortfolioEntity> userPortfolios = userPortfolioRepo.findAll();
        System.out.println("User Portfolio size: " + userPortfolios.size());
        
        for(UserPortfolioEntity userPortfolio : userPortfolios) {
            List<PortfolioValueHistoryEntity> portfolioValues = portfolioValueHistoryRepo.findByUserPortfolioIdOrderByDateTimeDesc(userPortfolio.getId());
            System.out.println("Portfolio Values size: " + portfolioValues.size());
            for (int i = 0; i < portfolioValues.size(); i++) {
                if (i > 300) {
                    System.out.println("Deleting: " + portfolioValues.get(i).getId());
                    portfolioValueHistoryRepo.delete(portfolioValues.get(i));
                }
            }
        }
    }

}