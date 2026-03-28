package com.Assignment.Trading.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.Assignment.Trading.Entity.Portfolio;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    Optional<Portfolio> findByTraderId(String traderId);
}
