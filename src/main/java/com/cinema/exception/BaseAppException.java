package com.cinema.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseAppException extends RuntimeException {

    private final HttpStatus status;

    public BaseAppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
