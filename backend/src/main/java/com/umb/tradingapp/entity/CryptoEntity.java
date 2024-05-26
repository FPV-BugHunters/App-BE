package com.umb.tradingapp.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cryptos")
public class CryptoEntity {

    @Id
    @Column(name = "id")
    private Long id;
    
    @Column()
    private String name;
    
    @Column()
    private String symbol;
    
    @Column()
    private String slug;
    
    @Column()
    private int cmc_rank;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "platform_id", referencedColumnName = "id")
    private CryptoPlatformEntity platform;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "crypto", cascade = CascadeType.ALL)
    private List<CryptoQuoteEntity> quotes;

    
    // @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    // @JoinColumn(name = "last_quote_id", referencedColumnName = "id")
    // private CryptoQuoteEntity last_quote;

    // @ManyToOne(cascade = CascadeType.ALL)
    // @JoinColumn(name = "crypto_id", referencedColumnName = "id")
    // private CryptoEntity cryptoId;

   

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

