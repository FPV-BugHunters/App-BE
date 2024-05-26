package com.umb.tradingapp.security.entity;

import com.umb.tradingapp.entity.CryptoEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Watchlist")
public class WatchlistEntity {
    @Id
    @GeneratedValue
    private Long id;    

    @ManyToOne
    @JoinColumn(name = "crypto_id")
    private CryptoEntity crypto;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}