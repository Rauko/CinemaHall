package com.cinema.dto;

import lombok.Data;

@Data
public class BankPaymentResponse {

    private boolean success;
    private String transactionId;
    private String message;

}
