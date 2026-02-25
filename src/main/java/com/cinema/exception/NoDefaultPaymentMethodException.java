package com.cinema.exception;

public class NoDefaultPaymentMethodException extends BaseAppException{
    public NoDefaultPaymentMethodException(){
        super("No default payment method found.");
    }
}
