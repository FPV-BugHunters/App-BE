package com.umb.tradingapp.entity;

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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "quote_id", referencedColumnName = "id")
    private CryptoQuoteEntity quote;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "rank_id", referencedColumnName = "id")
    private CryptoRankEntity rank;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "platform_id", referencedColumnName = "id")
    private CryptoPlatformEntity platform;

    //@OneToMany(mappedBy = "platform")
    //Set<CryptoPlatformEntity> tokens;

    //@OneToOne(mappedBy = "cryptoId", cascade = CascadeType.ALL)
    //@PrimaryKeyJoinColumn    
    //private CryptoPlatformEntity cryptoPlatform;

    //@OneToOne(mappedBy = "cryptoId", cascade = CascadeType.ALL)
    //@PrimaryKeyJoinColumn
    //private CryptoQuoteEntity cryptoQuote;

    //@OneToOne(mappedBy = "cryptoId", cascade = CascadeType.ALL)
    //@PrimaryKeyJoinColumn
    //private CryptoRankEntity cryptoRank;
}

