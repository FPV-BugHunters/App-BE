package com.umb.tradingapp.config;

import com.umb.tradingapp.dto.ListingLatestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.umb.tradingapp.service.ListingLatest;

@Service
public class DataScheduler {
    
    @Autowired
    ListingLatest listingLatest;

    @Scheduled(fixedRate=1000*1000)
    public void updateData() {

        // ll.loadDataHistorical("Solana","5y"); // historicke data cien
        //ll.loadDataListOfCoins(); // Coin k UUID, podla UUID vieme vytiahnut historicke data daneho coinu

        ListingLatestDTO listingLatestDTO = listingLatest.loadListingLatest();
        listingLatest.updateCryptos(listingLatestDTO);
        listingLatest.updateQuotes(listingLatestDTO);
        listingLatest.removeOldQuotes();
        // System.out.println(listingLatestDTO);
        
        // ll.saveCryptoId();
        // ll.saveCryptoPlatform();
        // ll.saveCryptoQuote();
        // ll.saveCryptoRank();
        // ll.loadHistoricalPricies();

        System.out.println("Task performed on: " + new Date().toString() + "n" + "Thread's name: " + Thread.currentThread().getName());
    }
}
