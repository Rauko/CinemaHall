package com.cinema.exception;

import org.springframework.http.HttpStatus;

//409
public class InvalidTicketStateException extends BaseAppException{
    public InvalidTicketStateException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
