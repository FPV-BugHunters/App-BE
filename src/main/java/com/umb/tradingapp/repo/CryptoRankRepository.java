package com.umb.tradingapp.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.umb.tradingapp.entity.CryptoRankEntity;

@Repository
public interface CryptoRankRepository extends CrudRepository<CryptoRankEntity, Long>{ }
