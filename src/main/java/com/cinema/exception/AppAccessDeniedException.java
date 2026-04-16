package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class AppAccessDeniedException extends BaseAppException{
    public AppAccessDeniedException(String message){
        super("Access denied. " + message,
                HttpStatus.FORBIDDEN,
                ErrorCode.ACCESS_DENIED);
    }
}
