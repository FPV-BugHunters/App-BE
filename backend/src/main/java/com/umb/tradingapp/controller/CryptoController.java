package com.umb.tradingapp.controller;

import com.umb.tradingapp.entity.CryptoIdEntity;
import com.umb.tradingapp.service.ListAllCrypto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CryptoController {

    @Autowired
    private ListAllCrypto listAllCrypto;

    @GetMapping("/api/cryptos")
    public Iterable<CryptoIdEntity> getAllCryptos() {
        return listAllCrypto.getData();
    }

}
