package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class UserSuspendedException extends BaseAppException{
    public UserSuspendedException(String username){
        super("User is SUSPENDED: " + username,
                HttpStatus.FORBIDDEN,
                ErrorCode.USER_SUSPENDED);
    }
}