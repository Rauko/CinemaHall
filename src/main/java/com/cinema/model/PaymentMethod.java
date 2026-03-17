package com.cinema.model;

import com.cinema.model.enums.PaymentMethodType;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "payment_methods")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
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
