package com.cinema.repository;

import com.cinema.model.BlockedEmail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockedEmailRepository extends JpaRepository<BlockedEmail, Long> {
    boolean existsByEmail(String email);
}
