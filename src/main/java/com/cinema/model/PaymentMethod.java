package com.cinema.model;

import com.cinema.model.enums.PaymentMethodType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "payment_methods")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentMethodType type;

    private String maskedDetails;

    private String providerToken;

    private boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
