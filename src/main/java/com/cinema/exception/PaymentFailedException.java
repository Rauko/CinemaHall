package com.cinema.exception;

public class PaymentFailedException extends BaseAppException{
    public PaymentFailedException() {
        super("Payment failed");
    }
}
