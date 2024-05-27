package com.umb.tradingapp.service;

/**
* This example uses the Apache HTTPComponents library.
*/

import java.util.Date;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.umb.tradingapp.dto.ListingLatestCryptoDataDTO;
import com.umb.tradingapp.dto.ListingLatestDTO;
import com.umb.tradingapp.dto.ListingLatestQuoteDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.umb.tradingapp.entity.CryptoEntity;
import com.umb.tradingapp.entity.CryptoQuoteEntity;
import com.umb.tradingapp.repo.CryptoQuoteRepository;
import com.umb.tradingapp.repo.CryptoRepository;

@Service
public class ListingLatest {

    @Value("${coinmarketcapUri}")
    private String coinmarketcapUri;

    @Autowired
    private CryptoRepository cryptoRepo;

    @Autowired
    private CryptoQuoteRepository cryptoQuoteRepo;

    @Autowired
    private ApiCall apiCall;


    public ListingLatestDTO loadListingLatest() {

        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("start", "1"));
        parameters.add(new BasicNameValuePair("limit", "100"));

        try {
            StringBuffer response = apiCall.execute(coinmarketcapUri + "/v1/cryptocurrency/listings/latest", parameters);

            ObjectMapper objectMapper = new ObjectMapper();
            ListingLatestDTO result = objectMapper.readValue(response.toString(), ListingLatestDTO.class);
            return result;

        } catch (IOException e) {
            System.out.println("Error: cannot access content - " + e.toString());

        } catch (URISyntaxException e) {
            System.out.println("Error: Invalid URL " + e.toString());
        }

        return null;
    }

    @Transactional
    public void updateCryptos(ListingLatestDTO listingLatestDTO) {
        for (ListingLatestCryptoDataDTO data : listingLatestDTO.getData()) {
            if (!cryptoRepo.existsById(data.getId())) {
                CryptoEntity cryptoId = new CryptoEntity();
                cryptoId.setId(data.getId());
                cryptoId.setName(data.getName());
                cryptoId.setSymbol(data.getSymbol());
                cryptoId.setSlug(data.getSlug());
                cryptoId.setCmc_rank(data.getCmc_rank());
                cryptoRepo.save(cryptoId);
            }
        }
    }

    @Transactional
    public void updateQuotes(ListingLatestDTO listingLatestDTO) {

        for (ListingLatestCryptoDataDTO data : listingLatestDTO.getData()) {
            if (data.getQuote() != null && data.getQuote().containsKey("USD")) {

                ListingLatestQuoteDTO quote = data.getQuote().get("USD");

                if (cryptoRepo.existsById(data.getId())) {
                    CryptoEntity crypto = cryptoRepo.findById(data.getId()).get();
                    CryptoQuoteEntity cryptoQuote = new CryptoQuoteEntity();
                    cryptoQuote.setPrice(quote.getPrice());
                    cryptoQuote.setPercentChange1h(quote.getPercent_change_1h());
                    cryptoQuote.setPercentChange24h(quote.getPercent_change_24h());
                    cryptoQuote.setPercentChange7d(quote.getPercent_change_7d());
                    cryptoQuote.setVolume24h(quote.getVolume_24h());
                    cryptoQuote.setMarketCap(quote.getMarket_cap());
                    cryptoQuote.setMarketCapDominance(quote.getMarket_cap_dominance());
                    cryptoQuote.setFullyDilutedMarketCap(quote.getFully_diluted_market_cap());
                    cryptoQuote.setCrypto(crypto);
                    cryptoQuote.setCirculatingSupply(data.getCirculating_supply());
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    String dateString = quote.getLast_updated();

                    try {
                        Date date = formatter.parse(dateString);
                        cryptoQuote.setLastUpdated(date);
                    } catch (ParseException e) {
                        System.out.println("Error parsing date: " + e.getMessage());
                    }

                    crypto.getQuotes().add(cryptoQuote);
                    cryptoRepo.save(crypto);
                }
            }
        }
    }



    public void removeOldQuotes() {
        
        List<CryptoEntity> cryptos = cryptoRepo.findAll();
        for (CryptoEntity crypto : cryptos) {
            List<CryptoQuoteEntity> quotes = cryptoQuoteRepo.findByCryptoIdOrderByLastUpdatedDesc(crypto.getId());

            for (int i = 0; i < quotes.size(); i++) {
                if (i > 300) {
                    cryptoQuoteRepo.delete(quotes.get(i));
                }
            }
        }
    }
}