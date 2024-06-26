package com.umb.tradingapp.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoPriceHistoryDTO {
    private Double priceUSD;
    private Date timestamp;
}
