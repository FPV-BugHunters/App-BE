package com.umb.tradingapp.entity;

import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "crypto_id")
public class CryptoIdEntity {

    @Id
    @Column(name = "id")
    private Long id;
    private String name;
    private String symbol;
    private String slug;

    @OneToOne(mappedBy = "cryptoId")
    @PrimaryKeyJoinColumn    
    private CryptoPlatformEntity cryptoPlatform;

    @OneToOne(mappedBy = "cryptoId")
    @PrimaryKeyJoinColumn
    private CryptoQuoteEntity cryptoQuote;

    @OneToMany(mappedBy = "platform")
    Set<CryptoPlatformEntity> tokens;

}

