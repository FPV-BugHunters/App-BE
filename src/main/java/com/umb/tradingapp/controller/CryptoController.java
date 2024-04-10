package com.umb.tradingapp.controller;

import com.umb.tradingapp.DTO.CryptoPriceDTO;
import com.umb.tradingapp.service.ApiService;
import com.umb.tradingapp.service.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CryptoController {

    @Autowired
    private CryptoService cs;
    @Autowired
    private ApiService as;

    @GetMapping("/api/cryptos")
    public List<CryptoPriceDTO> getAllCryptos() {
        as.saveCryptoNamesToDS();
        //return cs.listAllCryptos();
        return null;
    }

}
