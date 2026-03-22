package com.cinema.dto.payment.bankEmu;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankPaymentResponse {

    private boolean success;
    private String transactionId;
    private String message;

}
