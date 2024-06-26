package com.umb.tradingapp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class ApiCall {
    
    @Value("${coinmarketcapApiKey}")
    private String coinmarketcapApiKey;

    @Value("${coinmarketcapUri}")
    private String coinmarketcapUri;

    @Value("${coinrankingUri}")
    private String coinrankingUri;
    
    public StringBuffer execute(String uri, List<NameValuePair> parameters) throws URISyntaxException, IOException {

        // Client
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(uri);

        // Headers
        httpGet.addHeader("X-CMC_PRO_API_KEY", coinmarketcapApiKey);
        httpGet.addHeader("Accept", "application/json");

        // Execute
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

        System.out.println("GET Response Status:: " + httpResponse.getStatusLine().getStatusCode());

        BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();

        // print result
        httpClient.close();

        return response;
    }
    
}
