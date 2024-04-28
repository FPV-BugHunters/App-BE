package com.umb.tradingapp.repo;

import com.umb.tradingapp.entity.CryptoIdEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoIdRepository extends JpaRepository<CryptoIdEntity, Long> { }
