package com.umb.tradingapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "crypto_rank")
public class CryptoRankEntity {

    @Id
    @Column(name = "id")
    private Long id;
    private Integer cmcRank;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private CryptoIdEntity cryptoId;
    
}
