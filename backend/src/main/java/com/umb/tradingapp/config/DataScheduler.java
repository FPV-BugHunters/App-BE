package com.umb.tradingapp.config;

import com.umb.tradingapp.dto.ListingLatestDTO;

import java.util.Date;

import javax.sound.sampled.Port;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.umb.tradingapp.service.BalanceHistoryService;
import com.umb.tradingapp.service.ListingLatest;
import com.umb.tradingapp.service.PortfolioValueHistoryService;

@Service
public class DataScheduler {
    
    @Autowired
    ListingLatest listingLatest;
    

    @Autowired
    BalanceHistoryService  balanceHistory;
    
    @Autowired
    PortfolioValueHistoryService portfolioValueHistoryService;

    @Scheduled(fixedRate=1000*1000)
    public void updateData() {


        ListingLatestDTO listingLatestDTO = listingLatest.loadListingLatest();
        listingLatest.updateCryptos(listingLatestDTO);
        listingLatest.updateQuotes(listingLatestDTO);
        listingLatest.removeOldQuotes();

        balanceHistory.saveBalance();
        
        portfolioValueHistoryService.savePortfolioValue();
        

        
       System.out.println("Task performed on: " + new Date().toString() + "n" + "Thread's name: " + Thread.currentThread().getName());
    }
}
