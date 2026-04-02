package com.Assignment.Trading.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Assignment.Trading.Entity.Trader;

@Repository
public interface TraderRepository extends JpaRepository<Trader, Long> {
    Optional<Trader> findByTraderId(String traderId);
}
