package com.cinema.repository;

import com.cinema.model.PaymentMethod;
import com.cinema.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    List<PaymentMethod> findByUser(User user);

    Optional<PaymentMethod> findByUserAndIsDefaultTrue(User user);
}
