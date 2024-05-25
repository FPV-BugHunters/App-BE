package com.umb.tradingapp.entity;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "crypto_quotes")
public class CryptoQuoteEntity {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double fullyDilutedMarketCap;
    private Double marketCap;
    private Double price;
    private Double volume24h;
    private Double circulatingSupply;
    private Float volumeChange24h;
    private Double marketCapDominance;
    private Float percentChange1h;
    private Float percentChange24h;
    private Float percentChange7d;
    private Date lastUpdated;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "crypto_id", referencedColumnName = "id")
    private CryptoEntity cryptoId;

    // @OneToOne(mappedBy = "quote")
    // private CryptoEntity cryptoEntity;
    
    



}