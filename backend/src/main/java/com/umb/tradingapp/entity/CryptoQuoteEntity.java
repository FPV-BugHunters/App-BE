package com.umb.tradingapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "crypto_quote")
public class CryptoQuoteEntity {
    
    @Id
    @Column(name = "id")
    private Long id;
    private Double fullyDilutedMarketCap;
    private Double marketCap;
    private Double price;
    private Double volume24h;
    private Float volumeChange24h;
    private Float marketCapDominance;
    private Float percentChange1h;
    private Float percentChange24h;
    private Float percentChange7d;
    private Float percentChange30d;
    private Float percentChange60d;
    private Float percentChange90d;

}