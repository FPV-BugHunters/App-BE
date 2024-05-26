package com.umb.tradingapp.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.umb.tradingapp.entity.UserPortfolioEntity;

public interface UserPortfolioRepository extends JpaRepository<UserPortfolioEntity, Long>{
    List<UserPortfolioEntity> findByUserId(Long id);
}
