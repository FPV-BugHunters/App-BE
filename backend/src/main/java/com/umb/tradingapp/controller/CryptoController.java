package com.umb.tradingapp.controller;

import com.umb.tradingapp.dto.CryptoHistoryPriceDTO;
import com.umb.tradingapp.dto.CryptoPriceDTO;
import com.umb.tradingapp.service.CryptoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "GET historical price of symbol", description = " example: http://localhost:8080/api/crypto/history?symbol=ETH&timeframe=5y    " +
            "           //1h 3h 12h 24h 7d 30d 3m 1y 3y 5y")
    @ApiResponses(value = {

    })
    @GetMapping("/api/crypto/history") // http://localhost:8080/api/crypto/history?symbol=ETH&timeframe=5y
    public CryptoHistoryPriceDTO getCryptoPriceHistory(@RequestParam(name = "symbol") String cryptoSymbol,@RequestParam(name = "timeframe") String timeframe ) {
        return cryptoService.listCryptoHistoricalPrice(cryptoSymbol,timeframe);
    }

}
