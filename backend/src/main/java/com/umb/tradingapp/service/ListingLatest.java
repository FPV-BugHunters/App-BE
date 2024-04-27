package com.umb.tradingapp.service; /**
 * This example uses the Apache HTTPComponents library.
 */

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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

import com.umb.tradingapp.entity.CryptoIdEntity;
import com.umb.tradingapp.entity.CryptoPlatformEntity;
import com.umb.tradingapp.entity.CryptoQuoteEntity;
import com.umb.tradingapp.entity.CryptoRankEntity;
import com.umb.tradingapp.repo.CryptoIdRepository;
import com.umb.tradingapp.repo.CryptoPlatformRepository;
import com.umb.tradingapp.repo.CryptoQuoteRepository;
import com.umb.tradingapp.repo.CryptoRankRepository;

@Service
public class ListingLatest {

    @Autowired
    private CryptoIdRepository cryptoIdRepo;

    @Autowired
    private CryptoPlatformRepository cryptoPlatformRepo;

    @Autowired
    private CryptoQuoteRepository cryptoQuoteRepo;

    @Autowired
    private CryptoRankRepository cryptoRankRepo;

    @Value("${coinmarketcapApiKey}")
    private String coinmarketcapApiKey;

    @Value("${coinmarketcapUri}")
    private String coinmarketcapUri;

    private JSONArray dataArray;

    public void loadData() {

        System.out.println(coinmarketcapApiKey);
        System.out.println(coinmarketcapUri);

        List<NameValuePair> parameters = new ArrayList<>();

        parameters.add(new BasicNameValuePair("start", "1"));
        parameters.add(new BasicNameValuePair("limit","100"));

        try {
            String result = makeAPICall(coinmarketcapUri + "/v1/cryptocurrency/listings/latest", parameters);

            this.dataArray = new JSONObject(result).getJSONArray("data");  // Parse JSON response

        } catch (IOException e) {
            System.out.println("Error: cannot access content - " + e.toString());

        } catch (URISyntaxException e) {
            System.out.println("Error: Invalid URL " + e.toString());

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void saveCryptoId () {
        try {
            JSONObject o;
            for (int i = 0; i < this.dataArray.length(); i++) {
                o = this.dataArray.getJSONObject(i);
                Long cryptoId = Long.parseLong(o.getString("id"));
                CryptoIdEntity entity;

                if (cryptoIdRepo.existsById(cryptoId)) {
                    entity = cryptoIdRepo.getReferenceById(cryptoId);
                } else {
                    entity = new CryptoIdEntity();
                }

                entity.setId(Long.parseLong(o.getString("id")));
                entity.setName(o.getString("name"));
                entity.setSymbol(o.getString("symbol"));
                entity.setSlug(o.getString("slug"));
                entity.setRank(null);
                entity.setQuote(null);
                cryptoIdRepo.save(entity);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void saveCryptoPlatform () {
        try {
            JSONObject o;
            for (int i = 0; i < this.dataArray.length(); i++) {
                o = this.dataArray.getJSONObject(i);
                Long cryptoId = Long.parseLong(o.getString("id"));
                CryptoPlatformEntity entity;

                if (!o.getString("platform").equals("null")) {

                    JSONObject p = new JSONObject(o.getString("platform"));
                    Long platformId = Long.parseLong(p.getString("id"));

                    if (cryptoPlatformRepo.existsById(cryptoId)) {
                        entity = cryptoPlatformRepo.getReferenceById(cryptoId);
                    } else {
                        entity = new CryptoPlatformEntity();
                        entity.setId(cryptoId);
                    }

                    if (cryptoIdRepo.existsById(platformId)) {
                        CryptoIdEntity platform = cryptoIdRepo.getReferenceById(platformId);
                        CryptoIdEntity id = cryptoIdRepo.getReferenceById(cryptoId);
                        entity.setToken(p.getString("token_address"));
                        entity.setPlatform(platform);
                        cryptoPlatformRepo.save(entity);
                        id.setPlatform(entity);
                        cryptoIdRepo.save(id);
                    }
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void saveCryptoQuote () {
        try {
            JSONObject o;
            for (int i = 0; i < this.dataArray.length(); i++) {
                o = this.dataArray.getJSONObject(i);

                Long cryptoId = Long.parseLong(o.getString("id"));
                CryptoQuoteEntity entity;
                JSONObject q = o.getJSONObject("quote").getJSONObject("USD");

                if (cryptoQuoteRepo.existsById(cryptoId)) {
                    entity = cryptoQuoteRepo.getReferenceById(cryptoId);
                } else {
                    entity = new CryptoQuoteEntity();
                    entity.setId(cryptoId);
                }

                entity.setFullyDilutedMarketCap(Double.parseDouble(q.getString("fully_diluted_market_cap")));
                entity.setMarketCap(Double.parseDouble(q.getString("market_cap")));
                entity.setPrice(Double.parseDouble(q.getString("price")));
                entity.setVolume24h(Double.parseDouble(q.getString("volume_24h")));
                entity.setVolumeChange24h(Float.parseFloat(q.getString("volume_change_24h")));
                entity.setMarketCapDominance(Float.parseFloat(q.getString("market_cap_dominance")));
                entity.setPercentChange1h(Float.parseFloat(q.getString("percent_change_1h")));
                entity.setPercentChange24h(Float.parseFloat(q.getString("percent_change_24h")));
                entity.setPercentChange7d(Float.parseFloat(q.getString("percent_change_7d")));
                entity.setPercentChange30d(Float.parseFloat(q.getString("percent_change_30d")));
                entity.setPercentChange60d(Float.parseFloat(q.getString("percent_change_30d")));
                entity.setPercentChange90d(Float.parseFloat(q.getString("percent_change_90d")));

                cryptoQuoteRepo.save(entity);

                CryptoIdEntity id = cryptoIdRepo.getReferenceById(cryptoId);
                id.setQuote(entity);
                cryptoIdRepo.save(id);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

     @Transactional
     public void saveCryptoRank() {
        try {
            for (int i = 0; i < this.dataArray.length(); i++) {
                JSONObject o = this.dataArray.getJSONObject(i);
                long cryptoId = Long.parseLong(o.getString("id"));
                
                CryptoRankEntity entity;
                if (cryptoRankRepo.existsById(cryptoId)) {
                    entity = cryptoRankRepo.getReferenceById(cryptoId);
                } else {
                    entity = new CryptoRankEntity();
                    entity.setId(cryptoId);
                }
                entity.setCmcRank(Integer.parseInt(o.getString("cmc_rank")));

                cryptoRankRepo.save(entity);

                CryptoIdEntity id = cryptoIdRepo.getReferenceById(cryptoId);
                id.setRank(entity);
                cryptoIdRepo.save(id);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    

    public String makeAPICall(String uri, List<NameValuePair> parameters)
            throws URISyntaxException, IOException {
        String response_content = "";

        URIBuilder query = new URIBuilder(uri);
        query.addParameters(parameters);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(query.build());

        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.addHeader("X-CMC_PRO_API_KEY", coinmarketcapApiKey);

        CloseableHttpResponse response = client.execute(request);

        try {
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            response_content = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }

        return response_content;
    }

    
}