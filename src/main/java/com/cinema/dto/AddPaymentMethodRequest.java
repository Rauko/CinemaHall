package com.cinema.dto;

import com.cinema.model.enums.PaymentMethodType;
import lombok.Data;

@Data
public class AddPaymentMethodRequest {

    private PaymentMethodType type;

    private String cardNumber;  // for card only
    private String expirationDate;
    private String cvv;

    private boolean makeDefault;
}
