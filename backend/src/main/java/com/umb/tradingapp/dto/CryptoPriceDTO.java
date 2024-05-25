package com.umb.tradingapp.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoPriceDTO {
    private Long id;
    private String name;
    private String symbol;
    private Integer rank;
    private Double priceUSD;
    private Double circulatingSupply;
    private Double marketCap;
    private Double volume;
    private Float h1;
    private Float h24;
    private Float d7;
    private List<Double> priceHistoryUSD;
    
    

}
