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
    private Long id;
    private Integer rank;
    private String name;
    private String symbol;
    private String slug;

    @ManyToOne
    @JoinColumn(name = "platform", insertable = true, updatable = true)
    private CryptoEntity platform;

    @OneToMany(mappedBy = "platform")
    Set<CryptoEntity> tokens;
}

