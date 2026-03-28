package com.Assignment.Trading.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.Assignment.Trading.Entity.Holding;
import com.Assignment.Trading.Entity.Portfolio;

import jakarta.persistence.LockModeType;
@Repository
public interface HoldingRepository extends JpaRepository<Holding, Long> {

	Optional<Holding> findByPortfolioAndStock(Portfolio portfolio,String Stock);
    
    List<Holding> findByPortfolio(Portfolio portfolio);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT h FROM Holding h WHERE h.portfolio = :portfolio AND h.stock = :stock")
    Optional<Holding> findByPortfolioAndStockForUpdate(Portfolio portfolio, String stock);
}	
