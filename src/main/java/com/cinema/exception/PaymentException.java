package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class PaymentException extends BaseAppException {

    public PaymentException(String message) {
        super(message,
                HttpStatus.BAD_REQUEST,
                ErrorCode.PAYMENT_FAILED);
    }
}
