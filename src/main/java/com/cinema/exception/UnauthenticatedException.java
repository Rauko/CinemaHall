package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class UnauthenticatedException extends BaseAppException{
    public UnauthenticatedException(){
        super("No authenticated user.",
                HttpStatus.UNAUTHORIZED,
                ErrorCode.UNAUTHENTICATED);
    }
}
