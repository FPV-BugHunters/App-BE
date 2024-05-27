package com.umb.tradingapp.dto;



import java.util.Date;

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
    private Date dateTime;
}
