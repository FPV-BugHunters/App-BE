package com.umb.tradingapp.repo;

import com.umb.tradingapp.entity.CryptoEntity;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoRepository extends JpaRepository<CryptoEntity, Long> {
    Optional<CryptoEntity> findById(Long id);
}
