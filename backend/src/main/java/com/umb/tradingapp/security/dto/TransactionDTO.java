package com.umb.tradingapp.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private Long id;
    private Float amount;
    private Double pricePerUnit;
    private Double totalPrice;
    private Long userId;
    private Long cryptoId;
    private String cryptoName;
    private String cryptoSymbol;
    private String type;
}