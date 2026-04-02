package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class EmailBelongsToBannedUserException extends BaseAppException {

    public EmailBelongsToBannedUserException(String email) {
        super("Email " + email + " belongs to user, that was already banned.",
                HttpStatus.CONFLICT,
                ErrorCode.EMAIL_BELONGS_TO_BANNED_USER);
    }
}
