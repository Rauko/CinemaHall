package com.cinema.exception;

import org.springframework.http.HttpStatus;

//404
public class TicketNotFoundException extends BaseAppException {
    public TicketNotFoundException(Long id) {
        super("Ticket not found: " + id, HttpStatus.NOT_FOUND);
    }
}
