package com.umb.tradingapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.umb.tradingapp.entity.CryptoRankEntity;

@Repository
public interface CryptoRankRepository extends JpaRepository<CryptoRankEntity, Long>{ }
