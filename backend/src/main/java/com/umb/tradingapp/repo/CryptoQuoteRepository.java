package com.umb.tradingapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.umb.tradingapp.entity.CryptoQuoteEntity;

@Repository
public interface CryptoQuoteRepository extends JpaRepository<CryptoQuoteEntity, Long> { }
