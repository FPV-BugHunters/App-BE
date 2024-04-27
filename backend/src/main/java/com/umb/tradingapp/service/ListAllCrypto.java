package com.umb.tradingapp.service; /**
 * This example uses the Apache HTTPComponents library.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umb.tradingapp.entity.CryptoIdEntity;
import com.umb.tradingapp.repo.CryptoIdRepository;

@Service
public class ListAllCrypto {

    @Autowired
    private CryptoIdRepository cryptoIdRepo;

    public Iterable<CryptoIdEntity> getData() {

        
        Iterable<CryptoIdEntity> test = cryptoIdRepo.findAll();
        return test;
        
    }
    
}