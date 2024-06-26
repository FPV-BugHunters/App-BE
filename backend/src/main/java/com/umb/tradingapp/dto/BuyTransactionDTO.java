package com.umb.tradingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyTransactionDTO {
    private Float amount;
    private Long cryptoId;
    private Long userPortfolioId;
}
