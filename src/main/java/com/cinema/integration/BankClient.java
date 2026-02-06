package com.cinema.integration;

import com.cinema.dto.BankPaymentRequest;
import com.cinema.dto.BankPaymentResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;


// Fake Bank for demonstration only

@Component
public class BankClient {

    public BankPaymentResponse processPayment(BankPaymentRequest request) {
        BankPaymentResponse response = new BankPaymentResponse();

        // Network latency imitation
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 85% of success transactions
        boolean success = Math.random() < 0.85;

        if (success) {
            response.setSuccess(true);
            response.setTransactionId(UUID.randomUUID().toString());
            response.setMessage("Bank payment successful");
        } else {
            response.setSuccess(false);
            response.setMessage("Bank payment failed: Insufficient funds");
        }

        return response;
    }
}
