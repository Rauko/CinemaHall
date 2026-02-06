package com.cinema.dto;

import com.cinema.model.enums.PaymentMethodType;
import lombok.Data;

@Data
public class PaymentRequest {

    private PaymentMethodType method;

    // Card
    private String cardNumber;
    private String cvv;
    private String expirationDate;

    // Google Pay
    private String googlePayToken;
}
