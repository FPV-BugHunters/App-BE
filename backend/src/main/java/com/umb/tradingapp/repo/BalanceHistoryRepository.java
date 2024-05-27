package com.umb.tradingapp.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.umb.tradingapp.entity.BalanceHistoryEntity;
import com.umb.tradingapp.entity.CryptoPlatformEntity;

@Repository
public interface BalanceHistoryRepository extends JpaRepository<BalanceHistoryEntity, Long> {
    public BalanceHistoryEntity findByUserId(Long userId);
    List<BalanceHistoryEntity> findByUserIdOrderByDateTimeAsc(Long userId);   
}
