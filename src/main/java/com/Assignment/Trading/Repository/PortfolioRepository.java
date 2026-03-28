package com.Assignment.Trading.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Assignment.Trading.Entity.Portfolio;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    Optional<Portfolio> findByTraderId(String traderId);
}