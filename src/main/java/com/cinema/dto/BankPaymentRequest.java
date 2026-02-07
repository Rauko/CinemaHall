package com.cinema.dto;

import lombok.Data;

@Data
public class BankPaymentRequest {

    private String token;
    private double amount;

}
