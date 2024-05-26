package com.umb.tradingapp.entity;

import java.time.LocalDateTime;

import com.umb.tradingapp.security.entity.UserEntity;
import com.umb.tradingapp.type.TransactionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "transaction")
public class TransactionEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "crypto_id")
    private CryptoEntity crypto;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "amount")
    private Float amount;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "price_per_unit")
    private Double pricePerUnit;

    @Column(name = "date_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TransactionType type;
}
