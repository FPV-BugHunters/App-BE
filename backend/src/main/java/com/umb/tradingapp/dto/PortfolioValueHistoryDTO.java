package com.umb.tradingapp.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioValueHistoryDTO {
    private Long id;
    private Date dateTime;
    private Double value;
    private Integer portfolioId;
}