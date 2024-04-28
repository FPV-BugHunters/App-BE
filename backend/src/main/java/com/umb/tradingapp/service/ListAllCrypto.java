package com.umb.tradingapp.service; /**
 * This example uses the Apache HTTPComponents library.
 */

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umb.tradingapp.dto.CryptoPriceDTO;
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

    public Iterable<CryptoPriceDTO> getCryptoPrice() {
        List<CryptoPriceDTO> arrDto = new ArrayList<>();
        List<CryptoIdEntity> arrId = cryptoIdRepo.findAll();

        for (int i = 0; i < arrId.size(); i++) {
            CryptoPriceDTO dto = new CryptoPriceDTO();
            CryptoIdEntity id = arrId.get(i);

            dto.setId(id.getId());
            dto.setName(id.getName());
            dto.setSymbol(id.getSymbol());
            dto.setRank(id.getRank().getCmcRank());
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
    
}