package com.cinema.dto;

import lombok.Data;

@Data
public class BankPaymentRequest {

    private String cardNumber;
    private String cvv;
    private String expirationDate;
    private double amount;

}
