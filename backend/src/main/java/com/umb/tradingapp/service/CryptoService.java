package com.umb.tradingapp.service; /**
 * This example uses the Apache HTTPComponents library.
 */

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.umb.tradingapp.dto.CryptoPriceDTO;
import com.umb.tradingapp.entity.CryptoEntity;
import com.umb.tradingapp.entity.CryptoQuoteEntity;
import com.umb.tradingapp.repo.CryptoRepository;
import com.umb.tradingapp.repo.CryptoQuoteRepository;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class CryptoService {


    @Autowired
    ListingLatest ll;
    
    @Autowired
    private CryptoRepository cryptoRepo;

    @Autowired CryptoQuoteRepository cryptoQuoteRepo;

    public Iterable<CryptoEntity> getData() {

        Iterable<CryptoEntity> test = cryptoRepo.findAll();
        return test;
        
    }

    public Iterable<CryptoPriceDTO> listAllCrypto() {
        List<CryptoPriceDTO> arrDto = new ArrayList<>();
        List<CryptoEntity> arrId = cryptoRepo.findAll();

        for (int i = 0; i < arrId.size(); i++) {
            CryptoPriceDTO dto = new CryptoPriceDTO();
            CryptoEntity id = arrId.get(i);

            dto.setId(id.getId());
            dto.setName(id.getName());
            dto.setSymbol(id.getSymbol());
            dto.setRank(id.getCmc_rank());
            dto.setPriceUSD(id.getQuote().getPrice());
            dto.setCirculatingSupply(id.getQuote().getCirculatingSupply());
            dto.setMarketCap(id.getQuote().getMarketCap());
            dto.setVolume(id.getQuote().getVolume24h());
            dto.setH1(id.getQuote().getPercentChange1h());
            dto.setH24(id.getQuote().getPercentChange24h());
            dto.setD7(id.getQuote().getPercentChange7d());

            arrDto.add(dto);
        }

        return arrDto;
    }

    // public CryptoHistoryPriceDTO listCryptoHistoricalPrice(String symbol, String timeframe) {
        
    //     // CryptoHistoryPriceDTO dto = new CryptoHistoryPriceDTO();

    //     // dto = ll.loadDataHistorical(symbol,timeframe);

    //     // return dto;
    //         //CryptoEntity id = arrId.get(i);

    //         //dto.setName(id.getName());
    //         //  dto.setDataList();


    //         //arrDto.add(dto);


    //     //return arrDto;
    // }

    public Double getCryptoPrice(Long cryptoId) {
        CryptoQuoteEntity entity = cryptoQuoteRepo.getReferenceById(cryptoId);
        return entity.getPrice();
    }

    public boolean checkCryptoExists(Long cryptoId, HttpServletResponse response) {
        if (!cryptoRepo.existsById(cryptoId)) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.addHeader("CryptoId", "cryptoId not found");
            return false;
        }
        return true;
    }

}