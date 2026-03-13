package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

//403
public class AccessDeniedException extends BaseAppException {
    public AccessDeniedException() {
        super("Access denied",
                HttpStatus.FORBIDDEN,
                ErrorCode.ACCESS_DENIED);
    }
}
