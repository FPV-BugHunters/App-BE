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
public class ListingLatestQuoteDTO {
    
    private double price;
    private double volume_24h;
    private double volume_change_24h;
    private float percent_change_1h;
    private float percent_change_24h;
    private float percent_change_7d;
    private double market_cap;
    private double market_cap_dominance;
    private double fully_diluted_market_cap;
    private String last_updated;
}
