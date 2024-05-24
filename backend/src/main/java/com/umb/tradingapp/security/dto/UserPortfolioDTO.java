package com.umb.tradingapp.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPortfolioDTO {
    private Long id;
    private Integer numberOfItems;
    private Double totalBalance;
    private String name;
}
