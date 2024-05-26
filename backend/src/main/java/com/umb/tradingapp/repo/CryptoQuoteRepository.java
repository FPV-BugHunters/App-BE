package com.umb.tradingapp.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.umb.tradingapp.entity.CryptoQuoteEntity;

@Repository
public interface CryptoQuoteRepository extends JpaRepository<CryptoQuoteEntity, Long> {
    List<CryptoQuoteEntity> findByCryptoId(Long id);
    List<CryptoQuoteEntity> findByCryptoIdOrderByLastUpdatedDesc(Long id);
    List<CryptoQuoteEntity> findByCryptoIdOrderByLastUpdatedAsc(Long id);

}

    // private CryptoEntity cryptoId;
