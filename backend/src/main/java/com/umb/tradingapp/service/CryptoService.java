package com.umb.tradingapp.service; /**
 * This example uses the Apache HTTPComponents library.
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.Crypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.umb.tradingapp.dto.CryptoPriceDTO;
import com.umb.tradingapp.dto.CryptoPriceHistoryDTO;
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
        List<CryptoPriceDTO> cryptosPriceDTO = new ArrayList<>();
        List<CryptoEntity> cryptos = cryptoRepo.findAll();
        
        for (CryptoEntity crypto : cryptos) {
            CryptoPriceDTO dto = new CryptoPriceDTO();
            dto.setId(crypto.getId());
            dto.setName(crypto.getName());
            dto.setSymbol(crypto.getSymbol());
            CryptoQuoteEntity cryptoQuoteEntity = cryptoQuoteRepo.findByCryptoIdOrderByLastUpdatedDesc(crypto.getId()).get(0);
            dto.setPriceUSD(cryptoQuoteEntity.getPrice());
            dto.setH1(cryptoQuoteEntity.getPercentChange1h());
            dto.setH24(cryptoQuoteEntity.getPercentChange24h());
            dto.setD7(cryptoQuoteEntity.getPercentChange7d());
            dto.setCirculatingSupply(cryptoQuoteEntity.getCirculatingSupply());
            dto.setMarketCap(cryptoQuoteEntity.getMarketCap());
            dto.setVolume(cryptoQuoteEntity.getVolume24h());

            cryptosPriceDTO.add(dto);
            List<CryptoQuoteEntity> history = cryptoQuoteRepo.findByCryptoIdOrderByLastUpdatedDesc(crypto.getId());
            if(history.size() > 0) {
                List<CryptoPriceHistoryDTO> priceHistory = new ArrayList<>();
                for (CryptoQuoteEntity quote : history) {
                    CryptoPriceHistoryDTO historyDTO = new CryptoPriceHistoryDTO();
                    historyDTO.setPriceUSD(quote.getPrice());
                    historyDTO.setTimestamp(quote.getLastUpdated());
                    priceHistory.add(historyDTO);
                }
                dto.setPriceHistoryUSD(priceHistory);

            }
        }

        return cryptosPriceDTO;
    }


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