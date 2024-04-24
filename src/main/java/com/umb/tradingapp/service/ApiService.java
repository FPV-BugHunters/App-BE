package com.umb.tradingapp.service; /**
 * This example uses the Apache HTTPComponents library.
 */

import com.umb.tradingapp.dto.CryptoPriceDTO;
import com.umb.tradingapp.entity.CryptoIdEntity;
import com.umb.tradingapp.entity.CryptoPlatformEntity;
import com.umb.tradingapp.repo.CryptoIdRepository;
import com.umb.tradingapp.repo.CryptoPlatformRepository;
import com.umb.tradingapp.repo.CryptoQuoteRepository;

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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApiService {

    @Autowired
    private CryptoIdRepository cryptoIdRepo;
    @Autowired
    private CryptoPlatformRepository cryptoPlatformRepo;
    @Autowired
    private CryptoQuoteRepository cryptoQuoteRepo;

    private final String apiKey = "ff7d522c-72f5-4c84-9a3f-5d70cf143185";
    private final String latest_uri = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
    private JSONArray dataArray;

    public void setDataArray () {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("start", "1"));
        parameters.add(new BasicNameValuePair("limit","100"));

        try {
            String result = makeAPICall(latest_uri, parameters);

            this.dataArray = new JSONObject(result).getJSONArray("data");  // Parse JSON response

        } catch (IOException e) {
            System.out.println("Error: cannot access content - " + e.toString());
        } catch (URISyntaxException e) {
            System.out.println("Error: Invalid URL " + e.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveCryptoId () {
        try {
            JSONObject o;
            for (int i = 0; i < this.dataArray.length(); i++) {
                o = this.dataArray.getJSONObject(i);

                CryptoIdEntity entity = new CryptoIdEntity();
                entity.setId(Long.parseLong(o.getString("id")));
                entity.setName(o.getString("name"));
                entity.setSymbol(o.getString("symbol"));
                entity.setSlug(o.getString("slug"));

                cryptoIdRepo.save(entity);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveCryptoPlatform () {
        try {
            JSONObject o;
            for (int i = 0; i < this.dataArray.length(); i++) {
                o = this.dataArray.getJSONObject(i);

                if (!o.getString("platform").equals("null")) {
                    CryptoPlatformEntity entity = new CryptoPlatformEntity();
                    JSONObject p = new JSONObject(o.getString("platform"));
                    System.out.println(p);

                    if (cryptoIdRepo.existsById(Long.parseLong(o.getString("id"))) 
                    && cryptoIdRepo.existsById(Long.parseLong(p.getString("id")))) {
                        CryptoIdEntity platform = cryptoIdRepo.findById(Long.parseLong(p.getString("id"))).get();
                        entity.setCryptoId(cryptoIdRepo.findById(Long.parseLong(o.getString("id"))).get());
                        entity.setToken(p.getString("token_address"));
                        entity.setPlatform(platform);
                        cryptoPlatformRepo.save(entity);
                    }
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /*public boolean saveCryptoNamesToDS () {

        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("start", "1"));
        parameters.add(new BasicNameValuePair("limit","100"));

        try {
            String result = makeAPICall(latest_uri, parameters);

            JSONArray dataArray = new JSONObject(result).getJSONArray("data");  // Parse JSON response
            JSONObject o;

            for (int i = 0; i < dataArray.length(); i++) {
                o = dataArray.getJSONObject(i);

                CryptoIdEntity cryptoEntity = new CryptoIdEntity();
                cryptoEntity.setId(Long.parseLong(o.getString("id")));
                cryptoEntity.setRank(Integer.parseInt(o.getString("cmc_rank")));
                cryptoEntity.setName(o.getString("name"));
                cryptoEntity.setSymbol(o.getString("symbol"));
                cryptoEntity.setSlug(o.getString("slug"));
                cryptoEntity.setPlatform(null);

                if (!o.getString("platform").equals("null")) {
                    JSONObject p = new JSONObject(o.getString("platform"));

                    if (cryptoRepo.existsById(Long.parseLong(p.getString("id")))) {
                        CryptoIdEntity platform = cryptoRepo.findById(Long.parseLong(p.getString("id"))).get();
                        cryptoEntity.setPlatform(platform);
                    }
                    //System.out.println("id: " + p.getString("id"));
                    //System.out.println("meno: " + p.getString("name"));
                }

                cryptoRepo.save(cryptoEntity);
            }


        } catch (IOException e) {
            System.out.println("Error: cannot access content - " + e.toString());
        } catch (URISyntaxException e) {
            System.out.println("Error: Invalid URL " + e.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
    */

    public String makeAPICall(String uri, List<NameValuePair> parameters)
            throws URISyntaxException, IOException {
        String response_content = "";

        URIBuilder query = new URIBuilder(uri);
        query.addParameters(parameters);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(query.build());

        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.addHeader("X-CMC_PRO_API_KEY", apiKey);

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