package com.cinema.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankPaymentResponse {

    private boolean success;
    private String transactionId;
    private String message;

}
