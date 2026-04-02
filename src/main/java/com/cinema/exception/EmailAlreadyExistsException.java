package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends BaseAppException {

    public EmailAlreadyExistsException() {
        super("Email is already registered in system.",
                HttpStatus.CONFLICT,
                ErrorCode.EMAIL_ALREADY_EXIST);
    }
}
