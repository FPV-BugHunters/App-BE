package com.umb.tradingapp.repo;

import com.umb.tradingapp.entity.CryptoEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoRepository extends CrudRepository<CryptoEntity, Long> { }
