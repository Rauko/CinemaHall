package com.cinema.repository;

import com.cinema.model.PurchaseHistory;
import com.cinema.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseHistoryRepository
        extends JpaRepository<PurchaseHistory, Long> {

    List<PurchaseHistory> findByPurchaseHistoryId(Long id);

    List<PurchaseHistory> findByUserOrderByCreatedAtDesc(User user);

    List<PurchaseHistory> findByUserAndMadeAtBetween(
            User user,
            LocalDateTime start,
            LocalDateTime end
    );
}
