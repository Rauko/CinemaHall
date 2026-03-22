package com.cinema.dto.payment;

import com.cinema.model.enums.PaymentMethodType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodDto {
    private Long id;
    private PaymentMethodType type;
    private String maskedDetails;
    private boolean isDefault;
}
