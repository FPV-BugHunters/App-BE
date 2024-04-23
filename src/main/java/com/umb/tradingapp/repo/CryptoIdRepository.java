package com.umb.tradingapp.repo;

import com.umb.tradingapp.entity.CryptoIdEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoIdRepository extends CrudRepository<CryptoIdEntity, Long> { }
