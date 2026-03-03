package com.cinema.exception;

import org.springframework.http.HttpStatus;

//403
public class AccessDeniedException extends BaseAppException {
    public AccessDeniedException() {
        super("Access denied", HttpStatus.FORBIDDEN);
    }
}
