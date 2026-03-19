package com.cinema.dto;

import com.cinema.model.enums.PaymentMethodType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddPaymentMethodRequest {

    private PaymentMethodType type;

    private String cardNumber;  // for card only
    private String expirationDate;
    private String cvv;

    private boolean makeDefault;
}
