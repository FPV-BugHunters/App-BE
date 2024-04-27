package com.umb.tradingapp.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.umb.tradingapp.entity.CryptoPlatformEntity;

@Repository
public interface CryptoPlatformRepository extends CrudRepository<CryptoPlatformEntity, Long>{ }
