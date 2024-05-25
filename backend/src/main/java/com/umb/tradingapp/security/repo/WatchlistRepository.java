package com.umb.tradingapp.security.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.umb.tradingapp.security.entity.WatchlistEntity;

public interface WatchlistRepository extends JpaRepository<WatchlistEntity, Long>{
    public Optional<WatchlistEntity> findByUserIdAndCryptoId(Long userId, Long cryptoId);
    public void deleteByUserIdAndCryptoId(Long userId, Long cryptoId);
    public List<WatchlistEntity> findAllByUserId(Long userId);
}
