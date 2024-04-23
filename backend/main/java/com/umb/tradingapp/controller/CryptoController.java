package com.umb.tradingapp.controller;

import com.umb.tradingapp.DTO.CryptoDTO;
import com.umb.tradingapp.service.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CryptoController {

    @Autowired
    private CryptoService cs;

    @GetMapping("/api/cryptos")
    public List<CryptoDTO> getAllCryptos() {
        return cs.listAllCryptos();
    }

}
