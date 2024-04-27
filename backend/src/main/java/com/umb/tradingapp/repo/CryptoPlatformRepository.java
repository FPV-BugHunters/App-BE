package com.umb.tradingapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.umb.tradingapp.entity.CryptoPlatformEntity;

@Repository
public interface CryptoPlatformRepository extends JpaRepository<CryptoPlatformEntity, Long>{ }
