package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

//400
public class PaymentFailedException extends BaseAppException{
    public PaymentFailedException(String message) {
        super(message,
                HttpStatus.BAD_REQUEST,
                ErrorCode.PAYMENT_FAILED);
    }
}
