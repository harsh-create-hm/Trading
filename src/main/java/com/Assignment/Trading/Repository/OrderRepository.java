package com.Assignment.Trading.Repository;

//OrderRepository.java

import com.Assignment.Trading.Entity.Order;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

 // Count pending orders (non-locked version for reads)
 @Query("SELECT COUNT(o) FROM Order o WHERE o.traderId = :traderId AND o.status = 'PENDING'")
 long countPendingByTraderId(@Param("traderId") String traderId);

 // Count pending orders WITH LOCK (for place order validation)
 @Lock(LockModeType.PESSIMISTIC_WRITE)
 @Query("SELECT COUNT(o) FROM Order o WHERE o.traderId = :traderId AND o.status = 'PENDING'")
 long countPendingByTraderIdLocked(@Param("traderId") String traderId);

 // Find order with pessimistic lock
 @Lock(LockModeType.PESSIMISTIC_WRITE)
 @Query("SELECT o FROM Order o WHERE o.id = :id")
 Optional<Order> findByIdWithLock(@Param("id") Long id);

 // Find order without lock (for reads)
 @Query("SELECT o FROM Order o WHERE o.id = :id")
 Optional<Order> findByIdNoLock(@Param("id") Long id);
}