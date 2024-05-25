package com.umb.tradingapp.security.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.umb.tradingapp.security.entity.UserPortfolioEntity;

public interface UserPortfolioRepository extends JpaRepository<UserPortfolioEntity, Long>{
    List<UserPortfolioEntity> findByUserId(Long id);
}
