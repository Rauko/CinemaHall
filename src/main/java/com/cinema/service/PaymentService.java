package com.cinema.service;

import com.cinema.dto.BankPaymentRequest;
import com.cinema.exception.PaymentException;
import com.cinema.integration.BankClient;
import com.cinema.model.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final BankClient bankClient;

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
