package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;
import com.cinema.model.enums.UserStatus;
import org.springframework.http.HttpStatus;

public class InvalidUserStatusException extends BaseAppException {

    public InvalidUserStatusException(Long id, UserStatus wrongStatus) {
        super("User  " + id + " is NOT " + wrongStatus +".",
                HttpStatus.FORBIDDEN,
                ErrorCode.INVALID_USER_STATUS);
    }
}
