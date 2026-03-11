package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public abstract class BaseAppException extends RuntimeException {

    private final HttpStatus status;
    private final ErrorCode errorCode;

    protected BaseAppException(
            String message,
            HttpStatus status,
            ErrorCode errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

}
