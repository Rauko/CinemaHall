package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

//404
public class TicketNotFoundException extends BaseAppException {
    public TicketNotFoundException(Long id) {
        super("Ticket not found: " + id,
                HttpStatus.NOT_FOUND,
                ErrorCode.TICKET_NOT_FOUND);
    }
}
