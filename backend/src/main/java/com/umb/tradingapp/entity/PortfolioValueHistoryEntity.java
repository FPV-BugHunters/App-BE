package com.umb.tradingapp.entity;


import java.util.Date;

import com.umb.tradingapp.security.entity.UserEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "portfolio_value_history")
public class PortfolioValueHistoryEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    
    @Column(name = "balance")
    private Double value;

    @Column(name = "date_time", columnDefinition = "TIMESTAMP")
    private Date dateTime;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private UserEntity user;
    
    
    @ManyToOne()
    @JoinColumn(name = "portfolio_id")
    private UserPortfolioEntity userPortfolio;
}
