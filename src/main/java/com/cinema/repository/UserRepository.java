package com.cinema.repository;

import com.cinema.model.User;
import com.cinema.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    long deleteAllByStatus(UserStatus userStatus);
}