package com.umb.tradingapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.umb.tradingapp.service.ListingLatest;

@Service
public class DataScheduler {
    
    @Autowired
    ListingLatest ll;
    
    @Scheduled(fixedRate=1000*1000)
    public void updateData() {

        ll.loadDataHistorical(); // historicke data cien
        ll.loadDataListOfCoins(); // Coin k UUID, podla UUID vieme vytiahnut historicke data daneho coinu

        ll.loadData();
        ll.saveCryptoId();
        ll.saveCryptoPlatform();
        ll.saveCryptoQuote();
        ll.saveCryptoRank();

        System.out.println("Task performed on: " + new Date() + "n" +
          "Thread's name: " + Thread.currentThread().getName());
    }
}
