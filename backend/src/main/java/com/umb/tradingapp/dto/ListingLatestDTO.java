package com.umb.tradingapp.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListingLatestDTO {
    private List<ListingLatestCryptoDataDTO> data;
    private CoinMarketStatus status;
}