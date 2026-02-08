package com.cinema.repository;

import com.cinema.model.PurchaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseHistoryRepository
        extends JpaRepository<PurchaseHistory, Long> {

    List<PurchaseHistory> findByPurchaseHistoryId(Long id);
}
