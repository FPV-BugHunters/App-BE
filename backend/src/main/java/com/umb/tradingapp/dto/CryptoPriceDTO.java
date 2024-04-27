package com.umb.tradingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoPriceDTO {
    private String name;
    private String symbol;
    private int rank;
    private double priceUSD;
    private double circulatingSupply;
    private double marketCap;


}
