package com.cinema.exception;

import org.springframework.http.HttpStatus;

//400
public class PaymentFailedException extends BaseAppException{
    public PaymentFailedException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
