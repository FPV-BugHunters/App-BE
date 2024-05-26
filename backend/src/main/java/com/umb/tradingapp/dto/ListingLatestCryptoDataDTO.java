package com.umb.tradingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListingLatestCryptoDataDTO {
    private Long id;
    private String name;
    private String symbol;
    private String slug;
    private int cmc_rank;
    private int num_market_pairs;
    private double circulating_supply;
    private double total_supply;
    private double max_supply;
    private boolean infinite_supply;
    private String last_updated;
    private String date_added;
    private List<String> tags;
    private Object platform;
    private Object self_reported_circulating_supply;
    private Object self_reported_market_cap;
    private Map<String, ListingLatestQuoteDTO> quote;
}
