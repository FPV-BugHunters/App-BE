package com.umb.tradingapp.service; /**
 * This example uses the Apache HTTPComponents library.
 */

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.umb.tradingapp.dto.CryptoHistoryPriceDTO;
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

    @Value("${coinrankingApiKey}")
    private String coinrankingApiKey;

    @Value("${coinmarketcapUri}")
    private String coinmarketcapUri;

    @Value("${coinrankingUri}")
    private String coinrankingUri;

    private JSONArray dataArray;
    private CryptoHistoryPriceDTO cp;

    public void loadDataListOfCoins(){
       // https://developers.coinranking.com/api/documentation/coins/coins

        System.out.println(coinrankingApiKey);
        System.out.println(coinrankingUri);

        // "coin/UUID"
        //  BTC UUID = Qwsogvtv82FCd
        //  ostatne UUID = z druhej api, urlAllCoins

        //timePeriod (optional) String
        //Timeperiod where the change and history are based on
        //Default value: 24h
        //Allowed values:
        //1h 3h 12h 24h 7d 30d 3m 1y 3y 5y

        //referenceCurrencyUuid (optional) String
        //UUID of reference currency, in which all the prices are calculated. Defaults to US Dollar
        //Default value: yhjMzLPhuIDl

        List<NameValuePair> parameters = new ArrayList<>();
        //String timePeriod = "?timePeriod=5y";

        try {
            String result = makeAPICall(coinrankingUri + "/v2/coins", parameters);
            System.out.println(result);
            formatCryptoData(result);

        } catch (IOException e) {
            System.out.println("Error: cannot access content - " + e.toString());

        } catch (URISyntaxException e) {
            System.out.println("Error: Invalid URL " + e.toString());

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }
    public static void formatCryptoData(String jsonString) throws JSONException {
        JSONObject jsonObj = new JSONObject(jsonString);

        // Skontrolovať stav
        if (!jsonObj.getString("status").equals("success")) {
            System.out.println("Neúspešný pokus o získanie dát.");
            return;
        }

        JSONObject stats = jsonObj.getJSONObject("data").getJSONObject("stats");
        JSONArray coins = jsonObj.getJSONObject("data").getJSONArray("coins");

        // Vypis štatistík
        System.out.println("Štatistiky:");
        System.out.println("Celkový počet: " + stats.getInt("total"));
        System.out.println("Celkový počet mincí: " + stats.getInt("totalCoins"));
        System.out.println("Celkový počet trhov: " + stats.getInt("totalMarkets"));
        System.out.println("Celkový počet burz: " + stats.getInt("totalExchanges"));
        System.out.println("Celková trhová kapitalizácia: " + stats.getString("totalMarketCap"));
        System.out.println("Celkový 24h objem: " + stats.getString("total24hVolume"));
        System.out.println();

        // Vypis informácií o minciach
        for (int i = 0; i < coins.length(); i++) {
            JSONObject coin = coins.getJSONObject(i);
            System.out.println("Minca:");
            System.out.println("  UUID: " + coin.getString("uuid"));
            System.out.println("  Symbol: " + coin.getString("symbol"));
            System.out.println("  Názov: " + coin.getString("name"));
            /*
            System.out.println("  Farba: " + coin.getString("color"));
            System.out.println("  URL ikony: " + coin.getString("iconUrl"));
            System.out.println("  Trhová kapitalizácia: " + coin.getString("marketCap"));
            System.out.println("  Cena: " + coin.getString("price"));
            System.out.println("  Zoznam: " + coin.getLong("listedAt"));
            System.out.println("  Úroveň: " + coin.getInt("tier"));
            System.out.println("  Zmena: " + coin.getString("change"));
            System.out.println("  Poradie: " + coin.getInt("rank"));
            System.out.println("  URL: " + coin.getString("coinrankingUrl"));
            System.out.println("  24h objem: " + coin.getString("24hVolume"));
            System.out.println("  BTC cena: " + coin.getString("btcPrice"));
             */
            System.out.println();
        }
    }
    public CryptoHistoryPriceDTO loadDataHistorical(String symbol,String timeframe){

        System.out.println(coinrankingApiKey);
        System.out.println(coinrankingUri);

        // https://developers.coinranking.com/api/documentation/coins/coin-price-history
        // "coin/UUID"
        //  BTC UUID = Qwsogvtv82FCd
        //  ostatne UUID = z druhej api, urlAllCoins

        //timePeriod (optional) String
        //Timeperiod where the change and history are based on
        //Default value: 24h
        //Allowed values:
        //1h 3h 12h 24h 7d 30d 3m 1y 3y 5y

        //referenceCurrencyUuid (optional) String
        //UUID of reference currency, in which all the prices are calculated. Defaults to US Dollar
        //Default value: yhjMzLPhuIDl

        List<NameValuePair> parameters = new ArrayList<>();
        if (timeframe == null){
            timeframe= "7d";
        }


        parameters.add(new BasicNameValuePair("timePeriod", timeframe));
        //String UUID = "razxDUgYGNAdQ"; // UUID pre ETH
        //String timePeriod = "?timePeriod=5y";


        String uuid = findUuidBySymbol(symbol);
        System.out.println("UUID pre mincu so symbolom " + symbol + " je: " + uuid);
        cp = new CryptoHistoryPriceDTO();
        cp.setName(symbol);

        try {
            String result = makeAPICall(coinrankingUri + "/v2/coin/"+uuid+"/history", parameters);

            //System.out.println(dataArrayHistorical);
            System.out.println("result: "+result);
           return formatAndPrintResponse(result);

        } catch (IOException e) {
            System.out.println("Error: cannot access content - " + e.toString());

        } catch (URISyntaxException e) {
            System.out.println("Error: Invalid URL " + e.toString());

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public static String findUuidBySymbol(String symbol) {
        List<Coin> coins = new ArrayList<>();

            coins.add(new Coin("Qwsogvtv82FCd", "BTC", "Bitcoin"));
            coins.add(new Coin("razxDUgYGNAdQ", "ETH", "Ethereum"));
            coins.add(new Coin("HIVsRcGKkPFtW", "USDT", "Tether USD"));
            coins.add(new Coin("WcwrkfNI4FUAe", "BNB", "BNB"));
            coins.add(new Coin("zNZHO_Sjf", "SOL", "Solana"));
            coins.add(new Coin("VINVMYf0u", "stETH", "Lido Staked Ether"));
            coins.add(new Coin("aKzUVe4Hh_CON", "USDC", "USDC"));
            coins.add(new Coin("-l8Mn2pVlRs-p", "XRP", "XRP"));
            coins.add(new Coin("a91GCGd_u96cF", "DOGE", "Dogecoin"));
            coins.add(new Coin("67YlI0K1b", "TON", "Toncoin"));
            coins.add(new Coin("qzawljRxB5bYu", "ADA", "Cardano"));
            coins.add(new Coin("dvUj0CzDZ", "AVAX", "Avalanche"));
            coins.add(new Coin("xz24e0BjL", "SHIB", "Shiba Inu"));
            coins.add(new Coin("CiixT63n3", "wstETH", "Wrapped liquid staked Ether 2.0"));
            coins.add(new Coin("Mtfb0obXVh59u", "WETH", "Wrapped Ether"));
            coins.add(new Coin("x4WXHge-vvFY", "WBTC", "Wrapped BTC"));
            coins.add(new Coin("qUhEFk1I61atv", "TRX", "TRON"));
            coins.add(new Coin("25W7FG7om", "DOT", "Polkadot"));
            coins.add(new Coin("VLqpJwogdhHNb", "LINK", "Chainlink"));
            coins.add(new Coin("ZlZpzOJo43mIo", "BCH", "Bitcoin Cash"));
            coins.add(new Coin("_H5FVG9iW", "UNI", "Uniswap"));
            coins.add(new Coin("uW2tk-ILY0ii", "MATIC", "Polygon"));
            coins.add(new Coin("AWma-WzFHmKVQ", "FET", "Fetch.AI"));
            coins.add(new Coin("D7B1x_ks7WhV5", "LTC", "Litecoin"));
            coins.add(new Coin("aMNLwaUbY", "ICP", "Internet Computer (DFINITY)"));
            coins.add(new Coin("7C4Mh4xy1yDel", "RNDR", "Render Token"));
            coins.add(new Coin("03WI8NQPF", "PEPE", "PEPE"));
            coins.add(new Coin("MoTuySvg7", "DAI", "Dai"));
            coins.add(new Coin("Z96jIvLU7", "IMX", "Immutable X"));
            coins.add(new Coin("DCrsaMv68", "NEAR", "NEAR Protocol"));
            coins.add(new Coin("ncYFcP709", "CAKE", "PancakeSwap"));
            coins.add(new Coin("hnfQfsYfeIGUQ", "ETC", "Ethereum Classic"));
            coins.add(new Coin("jad286TjB", "HBAR", "Hedera"));
            coins.add(new Coin("cpjRxjFYD", "FDUSD", "First Digital USD"));
            coins.add(new Coin("BoI4ux0nd", "MNT", "Mantle"));
            coins.add(new Coin("ymQub4fuB", "FIL", "Filecoin"));
            coins.add(new Coin("qhd1biQ7M", "GRT", "The Graph"));
            coins.add(new Coin("V8GxkwWow", "KAS", "Kaspa"));
            coins.add(new Coin("pgv7xSFi6", "TAO", "Bittensor"));
            coins.add(new Coin("sZUrmToWF", "WIF", "dogwifhat"));
            coins.add(new Coin("mMPrMcB7", "STX", "Stacks"));
            coins.add(new Coin("PDKcptVnzJTmN", "OKB", "OKB"));
            coins.add(new Coin("7XWg41D1", "AR", "Arweave"));
            coins.add(new Coin("Knsels4_Ol-Ny", "ATOM", "Cosmos"));
            coins.add(new Coin("qFakph2rpuMOL", "MKR", "Maker"));
            coins.add(new Coin("FEbS54wxo4oIl", "VET", "VeChain"));
            coins.add(new Coin("PkY9BmsyW", "INJ", "Injective Protocol"));
            coins.add(new Coin("3mVx2FX_iJFp5", "XMR", "Monero"));
            coins.add(new Coin("exbfr2U-0", "USDE", "USDe"));
            coins.add(new Coin("B42IRxNtoYmwK", "THETA", "Theta Token"));

        for (Coin coin : coins) {
            if (coin.getSymbol().equalsIgnoreCase(symbol)) {
                return coin.getUuid();
            }
        }
        return "Minca so symbolom '" + symbol + "' nebola nájdená.";
    }

    private CryptoHistoryPriceDTO formatAndPrintResponse(String responseBody) throws JSONException {
        JSONObject jsonResponse = new JSONObject(responseBody);
        String status = jsonResponse.getString("status");
        System.out.println("Status: " + status);

        JSONObject data = jsonResponse.getJSONObject("data");
        String change = data.getString("change");
        System.out.println("Change: " + change);

        JSONArray history = data.getJSONArray("history");
        System.out.println("History:");

        for (int i = 0; i < history.length(); i++) {
            JSONObject entry = history.getJSONObject(i);
            String price = entry.getString("price");
            long timestamp = entry.getLong("timestamp");

            if(!price.equals("null"))
                try {
                    this.cp.getDataList().add(new TimestampPrice(timestamp,Double.parseDouble(price)));
                    // Pokračujte s ďalším spracovaním doubleValue
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number format for value: " + price);
                    // Ošetrite chybu parsovania
                }
            // dataList.add(new TimestampPrice(1573689600L, 188.53320286391812));
            System.out.println("  Timestamp: " + timestamp + ", Price: " + price);
        }
        return  this.cp;
    }
    public void loadData() {

        System.out.println(coinmarketcapApiKey);
        System.out.println(coinmarketcapUri);

        List<NameValuePair> parameters = new ArrayList<>();

        parameters.add(new BasicNameValuePair("start", "1"));
        parameters.add(new BasicNameValuePair("limit","100"));

        try {
            String result = makeAPICall(coinmarketcapUri + "/v1/cryptocurrency/listings/latest", parameters);

            this.dataArray = new JSONObject(result).getJSONArray("data");  // Parse JSON response
            System.out.println(this.dataArray);

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
                entity.setCirculatingSupply(Double.parseDouble(o.getString("circulating_supply")));

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