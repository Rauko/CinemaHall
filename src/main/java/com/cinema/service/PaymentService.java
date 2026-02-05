package com.cinema.service;

import com.cinema.model.User;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    public boolean processPayment(User user, double amount){

        // integration of Stripe/PayPal/LiqPay...

        return true; //placeholder PH

    }
}
