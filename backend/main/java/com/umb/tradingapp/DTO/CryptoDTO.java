package com.umb.tradingapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoDTO {
    private String name;
    private String symbol;
    private int rank;
    private double priceUSD;
    private double circulatingSupply;
    private double marketCap;


}
