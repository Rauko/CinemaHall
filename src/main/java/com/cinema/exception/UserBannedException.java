package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class UserBannedException extends BaseAppException {
    public UserBannedException(String username){
        super("User is BANNED: " + username,
                HttpStatus.FORBIDDEN,
                ErrorCode.USER_BANNED);
    }
}
