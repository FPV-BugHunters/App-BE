package com.umb.tradingapp.service; /**
 * This example uses the Apache HTTPComponents library.
 */

import com.umb.tradingapp.DTO.CryptoPriceDTO;
import com.umb.tradingapp.repo.CryptoRepository;
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
    private CryptoRepository cryptoRepo;
    private final String apiKey = "ff7d522c-72f5-4c84-9a3f-5d70cf143185";

    public List<CryptoPriceDTO> main() {
        List<CryptoPriceDTO> a = new ArrayList<>();
        String latest_uri = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";

        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("start","1"));

        try {
            String result = makeAPICall(latest_uri, parameters);
            System.out.println("///////////////////////////////////////////////");

            JSONObject responseJson = new JSONObject(result);  // Parse JSON response
            JSONArray dataArray = responseJson.getJSONArray("data");

            //System.out.println(dataArray);
            System.out.println(dataArray.getJSONObject(0));
            // Loop through each cryptocurrency
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject cryptoData = dataArray.getJSONObject(i);

                String name = cryptoData.getString("name");
                String symbol = cryptoData.getString("symbol");
                double circ_supply =Double.valueOf(cryptoData.getString("circulating_supply")) ;

                // Extract price data for USD and EUR (assuming they exist)
                double priceUSD = 0.0;
                double circulatingSupply = 0.0;
                double marketCap = 0;
                JSONObject quoteObject = cryptoData.getJSONObject("quote");
                //JSONObject dataObject = cryptoData.getJSONObject("data");

                if (quoteObject.has("USD")) {
                    priceUSD = quoteObject.getJSONObject("USD").getDouble("price");
                    marketCap = quoteObject.getJSONObject("USD").getDouble("market_cap");
                }

                JSONObject dataObject = responseJson.getJSONArray("data").getJSONObject(i);  // Assuming first element is Bitcoin
                //circulatingSupply = dataObject.getInt("circulating_supply");
                circulatingSupply = circ_supply;

                dataObject = responseJson.getJSONArray("data").getJSONObject(i);  //
                int rank = dataObject.getInt("cmc_rank");

                // Formatted output
                System.out.printf("%s (%s):\n", name, symbol);
                System.out.println("Rank: "+ rank);
                System.out.printf("USD: %.2f$\n", priceUSD);
                System.out.printf("Market cap: %.2f$\n", marketCap);
                System.out.printf("Circulating supply: %.2f\n", circulatingSupply);
                System.out.println("-------");

                CryptoPriceDTO d = new CryptoPriceDTO();
                d.setRank(rank);
                d.setName(name);
                d.setSymbol(symbol);
                d.setPriceUSD(priceUSD);
                d.setMarketCap(marketCap);
                d.setCirculatingSupply(circulatingSupply);

               a.add(d);
               a.get(0).getRank();
                System.out.printf(d.getName());
                System.out.println(a.get(0).getRank());

            }
            System.out.println(responseJson);

        } catch (IOException e) {
            System.out.println("Error: cannont access content - " + e.toString());
        } catch (URISyntaxException e) {
            System.out.println("Error: Invalid URL " + e.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return a;
    }

    public boolean saveCryptoNamesToDS () {
        String latest_uri = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/map";

        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("start", "1"));
        parameters.add(new BasicNameValuePair("limit","20"));

        try {
            String result = makeAPICall(latest_uri, parameters);
            System.out.println("///////////////////////////////////////////////");

            JSONArray dataArray = new JSONObject(result).getJSONArray("data");  // Parse JSON response

            System.out.println(dataArray);

        } catch (IOException e) {
            System.out.println("Error: cannot access content - " + e.toString());
        } catch (URISyntaxException e) {
            System.out.println("Error: Invalid URL " + e.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

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