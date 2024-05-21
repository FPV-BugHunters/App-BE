package com.umb.tradingapp.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioDTO {
    private Long cryptoId;
    private Integer rank;
    private String name;
    private String slug;
    private Double totalPrice;
    private Double price;
    private Double circulatingSupply;
    private Double marketCap;
    private Double volume;
    private Float amount;
    private Float h1;
    private Float h24;
    private Float d7;
}
