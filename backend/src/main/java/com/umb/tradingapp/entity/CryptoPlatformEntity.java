package com.umb.tradingapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "crypto_platform")
public class CryptoPlatformEntity {

    @Id
    @Column(name = "id")
    private Long id;
    private String token;

    @ManyToOne
    @JoinColumn(name = "platform", nullable = false, insertable = true, updatable = true)
    private CryptoIdEntity platform;


}
