package com.umb.tradingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoHistoryPriceDTO {
    private String name;
    private List<TimestampPrice> dataList = new ArrayList<>();

}
