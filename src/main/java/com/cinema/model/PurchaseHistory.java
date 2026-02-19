package com.cinema.model;

import com.cinema.model.enums.PaymentStatus;
import com.cinema.model.enums.PurchaseActionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Ticket ticket;

    private double amount;

    @Enumerated(EnumType.STRING)
    private PurchaseActionType action;

    private LocalDateTime purchaseTime;

    private PaymentStatus paymentStatus;
}
