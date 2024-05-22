package com.umb.tradingapp.security.entity;

import com.umb.tradingapp.entity.CryptoIdEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
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
@Table(name = "portfolio")
public class PortfolioEntity {
    @Id
    @GeneratedValue
    @Column(name = "portfolio_id")    
    private Long id;

    @OneToOne
    @JoinColumn(name = "crypto_id")
    private CryptoIdEntity cryptoId;
    
    @ManyToOne
    @JoinColumn(name = "user_portfolio_id")
    private UserPortfolioEntity userPortfolio;

}
