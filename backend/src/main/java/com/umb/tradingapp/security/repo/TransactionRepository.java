package com.umb.tradingapp.security.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.umb.tradingapp.security.entity.TransactionEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long>{
    public List<TransactionEntity> findByUserId(Long userId);  
}
