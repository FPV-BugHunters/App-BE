package com.umb.tradingapp.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceHistoryDTO {
    private Long id;
    private Double balance;
    private Date dateTime;
}
