package com.cinema.dto.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankPaymentRequest {

    private String token;
    private double amount;

}
