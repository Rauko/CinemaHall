package com.cinema.repository;

import com.cinema.model.PurchaseHistory;
import com.cinema.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseHistoryRepository
        extends JpaRepository<PurchaseHistory, Long> {

    List<PurchaseHistory> findByUserOrderByPurchaseTimeDesc(User user);

    List<PurchaseHistory> findByUserAndPurchaseTimeBetween(
            User user,
            LocalDateTime start,
            LocalDateTime end
    );

    @Query("""
            SELECT COALESCE(SUM(p.amount), 0)
            FROM PurchaseHistory p
            WHERE p.action = 
                        com.cinema.model.enums.PurchaseActionType.PURCHASE
                    AND p.purchaseTime BETWEEN :start AND :end
            """)
    double sumRevenueBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
