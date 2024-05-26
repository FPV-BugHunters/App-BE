package com.umb.tradingapp.controller;

import com.umb.tradingapp.dto.CryptoPriceDTO;
import com.umb.tradingapp.service.CryptoService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CryptoController {

    @Autowired
    private CryptoService cryptoService;

    @Operation(summary = "GET all cryptos", description = " Available cryptos with their current price without login")
    @GetMapping("/api/cryptos")
    public Iterable<CryptoPriceDTO> getAllCryptos() {
        return cryptoService.listAllCrypto();
    }

    // @Operation(summary = "GET historical price of symbol", description = " example: http://localhost:8080/api/crypto/history?symbol=ETH&timeframe=5y            //1h 3h 12h 24h 7d 30d 3m 1y 3y 5y")
    // @ApiResponses(value = { })
    // @GetMapping("/api/crypto/history") // http://localhost:8080/api/crypto/history?symbol=ETH&timeframe=5y
    // public CryptoHistoryPriceDTO getCryptoPriceHistory(@RequestParam(name = "symbol") String cryptoSymbol,@RequestParam(name = "timeframe") String timeframe ) {
    //     return cryptoService.listCryptoHistoricalPrice(cryptoSymbol,timeframe);
    // }

}
