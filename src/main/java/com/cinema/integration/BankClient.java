package com.cinema.integration;

import com.cinema.dto.BankPaymentRequest;
import org.springframework.stereotype.Component;

import java.util.Random;


// Fake Bank for demonstration only

@Component
public class BankClient {

    private final Random random = new Random();

    public boolean charge(BankPaymentRequest request) {
        // SIMULATION
        // 90%
        boolean success = random.nextInt(10) != 0;

        //Remove when real bank connected
        System.out.println("Charging token: " + request.getToken());
        System.out.println("Amount: " + request.getAmount());
        System.out.println("Result: " + success);

        return success;
    }
}
