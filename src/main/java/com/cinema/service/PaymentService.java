package com.cinema.service;

import com.cinema.dto.payment.BankPaymentRequest;
import com.cinema.exception.PaymentException;
import com.cinema.integration.BankClient;
import com.cinema.model.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final BankClient bankClient;

    public void processPayment(PaymentMethod method,
                               double amount){

        log.info("Payment started: methodType={}, amount={}",
                method.getType(),
                amount
        );

        if (method.getType() != null) {
            processPaymentInternal(method, amount);
        } else {
            log.warn("Payment rejected: unsupported payment method, methodType={}",
                    (Object) null
            );
            throw new PaymentException("Unsupported payment method");
        }

        // integration of Stripe/PayPal/LiqPay...

        //may add transactionID keeping in DB

        log.info("Payment successful: methodType={}, amount={}",
                method.getType(),
                amount
        );

    }

    private void processPaymentInternal(PaymentMethod method,
                                    double amount) {
        BankPaymentRequest bankRequest = new BankPaymentRequest();
        bankRequest.setToken(method.getProviderToken());
        bankRequest.setAmount(amount);

        boolean success = bankClient.charge(bankRequest);

        if(!success){

            log.warn("Payment failed: methodType={}, amount={}",
                    method.getType(),
                    amount
            );

            throw new PaymentException("Payment Failed");
        }

    }
}
