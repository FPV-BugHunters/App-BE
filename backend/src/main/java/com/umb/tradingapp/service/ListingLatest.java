package com.umb.tradingapp.service;

import java.io.BufferedReader;

/**
* This example uses the Apache HTTPComponents library.
*/

import java.util.Date;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.umb.tradingapp.dto.CryptoHistoryPriceDTO;
import com.umb.tradingapp.dto.ListingLatestCryptoDataDTO;
import com.umb.tradingapp.dto.ListingLatestDTO;
import com.umb.tradingapp.dto.ListingLatestQuoteDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umb.tradingapp.dto.Coin;
import com.umb.tradingapp.dto.TimestampPrice;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.umb.tradingapp.entity.CryptoEntity;
import com.umb.tradingapp.entity.CryptoPlatformEntity;
import com.umb.tradingapp.entity.CryptoQuoteEntity;
import com.umb.tradingapp.repo.CryptoPlatformRepository;
import com.umb.tradingapp.repo.CryptoQuoteRepository;
import com.umb.tradingapp.repo.CryptoRepository;

import io.swagger.v3.oas.models.responses.ApiResponse;

@Service
public class ListingLatest {

    @Value("${coinmarketcapUri}")
    private String coinmarketcapUri;

    @Autowired
    private CryptoRepository cryptoRepo;

    @Autowired
    private CryptoPlatformRepository cryptoPlatformRepo;

    @Autowired
    private CryptoQuoteRepository cryptoQuoteRepo;

    @Autowired
    private ApiCall apiCall;

    private JSONArray dataArray;
    private CryptoHistoryPriceDTO cp;

    public ListingLatestDTO loadListingLatest() {

        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("start", "1"));
        parameters.add(new BasicNameValuePair("limit", "100"));

        try {
            StringBuffer response = apiCall.execute(coinmarketcapUri + "/v1/cryptocurrency/listings/latest",
                    parameters);

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
                System.out.println("Crypto updated: " + data.getName());
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


    @Transactional
    public void removeOldQuotes() {

        List<CryptoEntity> cryptos = cryptoRepo.findAll();
        for (CryptoEntity crypto : cryptos) {
            List<CryptoQuoteEntity> quotes = cryptoQuoteRepo.findByCryptoIdOrderByLastUpdatedDesc(crypto.getId());

            for (int i = 0; i < quotes.size(); i++) {
                if (i > 100) {
                    cryptoQuoteRepo.delete(quotes.get(i));
                    System.out.println("Quote deleted: " + quotes.get(i).getId());
                }
            }
        }
    }
}