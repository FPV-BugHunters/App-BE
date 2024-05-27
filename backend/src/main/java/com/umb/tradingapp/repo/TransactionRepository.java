package com.umb.tradingapp.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.umb.tradingapp.entity.TransactionEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long>{
    public List<TransactionEntity> findByUserId(Long userId);  
    public List<TransactionEntity> findByUserPortfolioId(Long userPortfolioId);
    public List<TransactionEntity> findByUserIdAndUserPortfolioIdOrderByDateTimeDesc(Long userId, Long userPortfolioId);

}
