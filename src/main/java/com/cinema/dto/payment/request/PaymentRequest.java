package com.cinema.dto.payment.request;

import com.cinema.model.enums.PaymentMethodType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {

    private PaymentMethodType method;

    // Card
    private String cardNumber;
    private String cvv;
    private String expirationDate;

    // Google Pay
    private String googlePayToken;
}
