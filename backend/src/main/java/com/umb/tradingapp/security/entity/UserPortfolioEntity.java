package com.umb.tradingapp.security.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_portfolio")
public class UserPortfolioEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long portfolioId;

    @Column(name = "name")
    private String name;

    @Column(name = "number_of_items")
    private Integer numberOfItems;

    @Column(name = "total_balance")
    private Double totalBalance;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "userPortfolio")
    private Set<PortfolioEntity> portfolios;
}
