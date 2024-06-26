package com.umb.tradingapp.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.umb.tradingapp.entity.PortfolioEntity;

public interface PortfolioRepository extends JpaRepository<PortfolioEntity, Long>{
    public Optional<PortfolioEntity> findByUserPortfolioIdAndCryptoId(Long userPortfolioId, Long cryptoId);
    public Boolean existsByUserPortfolioIdAndCryptoId(Long userPortfolioId, Long cryptoId);
    public PortfolioEntity getReferenceByUserPortfolioIdAndCryptoId(Long userPortfolioId, Long cryptoId);

    @Query(value = "SELECT SUM(price * amount) FROM portfolio p join crypto_quotes c ON p.crypto_id = c.id WHERE p.user_portfolio_id = ?1",
    nativeQuery = true)
    public Double getTotalPrice(Long userPortfolioId);

    @Query(value = "SELECT COUNT(user_portfolio_id) FROM portfolio WHERE user_portfolio_id = ?1",
    nativeQuery = true)
    public Integer getItemCount(Long userPortfolioId);
    public List<PortfolioEntity> findByUserPortfolioId(Long userPortfolioId);
}


