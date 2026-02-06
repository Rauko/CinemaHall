package com.cinema.service;

import com.cinema.dto.BankPaymentRequest;
import com.cinema.dto.BankPaymentResponse;
import com.cinema.integration.BankClient;
import com.cinema.model.User;
import com.cinema.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final BankClient bankClient;

    public PaymentService(UserRepository userRepository) {
        this.bankClient = new BankClient();
    }

    public void processPayment(User user, double amount){

        // integration of Stripe/PayPal/LiqPay...

        // TODO: real bank
        BankPaymentRequest request = new BankPaymentRequest();
        request.setCardNumber("4111 1111 1111 1111");
        request.setCvv("123");
        request.setExpirationDate("12/28");
        request.setAmount(amount);

        BankPaymentResponse response = bankClient.processPayment(request);

        if(!response.isSuccess()){
            throw new RuntimeException("Bank Payment Failed: " + response.getMessage());
        }

        // TODO: real bank
        //may add transactionID keeping in DB

    }
}
