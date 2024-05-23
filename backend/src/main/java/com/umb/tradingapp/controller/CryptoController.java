package com.umb.tradingapp.controller;

import com.umb.tradingapp.dto.CryptoHistoryPriceDTO;
import com.umb.tradingapp.dto.CryptoPriceDTO;
import com.umb.tradingapp.service.CryptoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CryptoController {

    @Autowired
    private CryptoService cryptoService;

    @GetMapping("/api/cryptos")
    public Iterable<CryptoPriceDTO> getAllCryptos() {
        return cryptoService.listAllCrypto();
    }

    @GetMapping("/api/crypto/history") // http://localhost:8080/api/crypto/history?symbol=Ethereum&timeframe=5y
    public CryptoHistoryPriceDTO getCryptoPriceHistory(@RequestParam(name = "symbol") String cryptoSymbol,@RequestParam(name = "timeframe") String timeframe ) {
        return cryptoService.listCryptoHistoricalPrice(cryptoSymbol,timeframe);
    }

}
