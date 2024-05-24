package com.umb.tradingapp.security.entity;

import com.umb.tradingapp.entity.CryptoIdEntity;

import jakarta.persistence.Column;
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
@Table(name = "portfolio")
public class PortfolioEntity {
    @Id
    @GeneratedValue
    @Column(name = "portfolio_id")    
    private Long id;

    @Column(name = "amount")
    private Float amount;

    @ManyToOne
    @JoinColumn(name = "crypto_id")
    private CryptoIdEntity crypto;
    
    @ManyToOne
    @JoinColumn(name = "user_portfolio_id")
    private UserPortfolioEntity userPortfolio;

    public void increaseAmount(Float add) {
        this.amount += add;
    }

    public void decreaseAmount(Float remove) {
        this.amount -= remove;
    }

    public Double getTotalPrice() {
        return this.getPricePerUnit() * this.amount;
    }

    public Double getPrice(Float amount) {
        return this.getPricePerUnit() * amount;
    }

    public Double getPricePerUnit() {
        return this.crypto.getQuote().getPrice();
    }

}
