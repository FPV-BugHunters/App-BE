package com.umb.tradingapp.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.umb.tradingapp.entity.BalanceHistoryEntity;
import com.umb.tradingapp.entity.CryptoPlatformEntity;
import com.umb.tradingapp.entity.PortfolioValueHistoryEntity;

@Repository
public interface PortfolioValueHistoryRepository  extends JpaRepository<PortfolioValueHistoryEntity, Long> {
    List<PortfolioValueHistoryEntity> findByUserIdOrderByDateTimeAsc(Long userId);
}
