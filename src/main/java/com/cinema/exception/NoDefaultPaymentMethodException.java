package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class NoDefaultPaymentMethodException extends BaseAppException{
    public NoDefaultPaymentMethodException(){
        super("No default payment method found.",
                HttpStatus.NO_CONTENT,
                ErrorCode.NO_DEFAULT_PAYMENT_METHOD);
    }
}
