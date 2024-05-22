package com.umb.tradingapp.security.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.umb.tradingapp.security.entity.PortfolioEntity;

public interface PortfolioRepository extends JpaRepository<PortfolioEntity, Long>{
    
}
