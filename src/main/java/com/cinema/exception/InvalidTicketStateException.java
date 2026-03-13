package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

//409
public class InvalidTicketStateException extends BaseAppException{
    public InvalidTicketStateException(String message) {
        super(message,
                HttpStatus.CONFLICT,
                ErrorCode.INVALID_TICKET_STATE);
    }
}
