package com.umb.tradingapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "crypto")
public class CryptoEntity {

    @Id
    @Column(name = "crypto_id")
    private Long crypto_id;
    private Integer rank;
    private String name;
    private String symbol;
    private String slug;
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "platform", insertable = false, updatable = false)
    private CryptoEntity platform;

    @OneToMany(mappedBy = "platform")
    Set<CryptoEntity> tokens;
}

