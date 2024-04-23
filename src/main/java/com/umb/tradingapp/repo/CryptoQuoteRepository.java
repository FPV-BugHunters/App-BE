package com.umb.tradingapp.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.umb.tradingapp.entity.CryptoQuoteEntity;

@Repository
public interface CryptoQuoteRepository extends CrudRepository<CryptoQuoteEntity, Long> { }
