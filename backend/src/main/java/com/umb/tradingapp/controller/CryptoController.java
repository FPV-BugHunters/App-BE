package com.umb.tradingapp.controller;

import com.umb.tradingapp.dto.CryptoPriceDTO;
import com.umb.tradingapp.entity.CryptoIdEntity;
import com.umb.tradingapp.service.ListingLatest;
import com.umb.tradingapp.service.CryptoService;
import com.umb.tradingapp.service.ListAllCrypto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CryptoController {

    @Autowired
    private CryptoService cs;
    
    @Autowired
    private ListingLatest as;
    

    @Autowired
    private ListAllCrypto listAllCrypto;

    @GetMapping("/api/cryptos")
    public Iterable<CryptoIdEntity> getAllCryptos() {

        

        return listAllCrypto.getData();
    }

}
