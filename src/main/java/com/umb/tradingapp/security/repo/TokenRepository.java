package com.umb.tradingapp.security.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.umb.tradingapp.security.entity.TokenEntity;

import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<TokenEntity, Long> {

    Optional<TokenEntity> findByToken(String token);
    Long deleteByToken(String token);

}
