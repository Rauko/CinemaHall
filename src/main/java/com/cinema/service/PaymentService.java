package com.cinema.service;

import com.cinema.dto.BankPaymentRequest;
import com.cinema.dto.BankPaymentResponse;
import com.cinema.dto.PaymentRequest;
import com.cinema.exception.PaymentException;
import com.cinema.integration.BankClient;
import com.cinema.model.PaymentMethod;
import com.cinema.model.User;
import com.cinema.model.enums.PaymentMethodType;
import com.cinema.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final BankClient bankClient;

    public PaymentService(UserRepository userRepository) {
        this.bankClient = new BankClient();
    }

    public void processPayment(PaymentMethod method,
                               double amount){

        if (method.getType() != null) {
            processPaymentInternal(method, amount);
        } else {
            throw new PaymentException("Unsupported payment method");
        }

        // integration of Stripe/PayPal/LiqPay...

        //may add transactionID keeping in DB

    }

    private void processPaymentInternal(PaymentMethod method,
                                    double amount) {
        BankPaymentRequest bankRequest = new BankPaymentRequest();
        bankRequest.setToken(method.getProviderToken());
        bankRequest.setAmount(amount);

        boolean success = bankClient.charge(bankRequest);

        if(!success){
            throw new PaymentException("Payment Failed");
        }

    }
}
