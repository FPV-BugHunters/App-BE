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

import com.umb.tradingapp.dto.BalanceHistoryDTO;
import com.umb.tradingapp.dto.ListingLatestCryptoDataDTO;
import com.umb.tradingapp.dto.ListingLatestDTO;
import com.umb.tradingapp.dto.ListingLatestQuoteDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.umb.tradingapp.entity.BalanceHistoryEntity;
import com.umb.tradingapp.entity.CryptoEntity;
import com.umb.tradingapp.entity.CryptoQuoteEntity;
import com.umb.tradingapp.repo.BalanceHistoryRepository;
import com.umb.tradingapp.repo.CryptoQuoteRepository;
import com.umb.tradingapp.repo.CryptoRepository;
import com.umb.tradingapp.security.entity.UserEntity;
import com.umb.tradingapp.security.repo.UserRepository;

@Service
public class BalanceHistoryService {

    @Value("${coinmarketcapUri}")
    private String coinmarketcapUri;

    @Autowired
    private CryptoRepository cryptoRepo;

    @Autowired
    private CryptoQuoteRepository cryptoQuoteRepo;

    @Autowired
    private ApiCall apiCall;
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private BalanceHistoryRepository balanceHistoryRepo;

    @Async
    @Transactional
    public void saveBalance() {
        List<UserEntity> users = userRepo.findAll();
        for (UserEntity user : users) {
            BalanceHistoryEntity historyBalance = new BalanceHistoryEntity();
            historyBalance.setBalance(user.getBalance());
            historyBalance.setDateTime(new Date());
            historyBalance.setUser(user);
            balanceHistoryRepo.save(historyBalance);
        }
    }
    

    public List<BalanceHistoryDTO> getBalanceHistory(Long userId) {

        Pageable pageable = PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "dateTime"));
        List<BalanceHistoryDTO> balanceHistoryDTO = new ArrayList<>();
        List<BalanceHistoryEntity> balanceHistory = balanceHistoryRepo.findAll(pageable).getContent();
        for (BalanceHistoryEntity balance : balanceHistory) {
            if (balance.getUser().getId() == userId) {
                BalanceHistoryDTO balanceDTO = new BalanceHistoryDTO();
                balanceDTO.setId(balance.getId());
                balanceDTO.setBalance(balance.getBalance());
                balanceDTO.setDateTime(balance.getDateTime());
                balanceHistoryDTO.add(balanceDTO);
            }
        }
        return balanceHistoryDTO;
    }

    @Transactional
    public void removeOldBalanceHistory() {
        
        Number size = 300;
        List<UserEntity> users = userRepo.findAll();
        for (UserEntity user : users) {
            List<BalanceHistoryEntity> balanceHistory = balanceHistoryRepo.findByUserIdOrderByDateTimeAsc(user.getId());
            if (balanceHistory.size() > size.intValue()) {
                for (int i = 0; i < balanceHistory.size() - size.intValue(); i++) {
                    balanceHistoryRepo.delete(balanceHistory.get(i));
                    System.out.println("Deleted balance history: " + balanceHistory.get(i).getId());
                }
            }
        }
    }

}