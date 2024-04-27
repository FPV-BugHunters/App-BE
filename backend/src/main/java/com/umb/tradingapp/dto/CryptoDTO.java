package com.umb.tradingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoDTO {
    private Long crypto_id;
    private Integer rank;
    private String name;
    private String symbol;
    private String slug;
    private Boolean isActive;
}
