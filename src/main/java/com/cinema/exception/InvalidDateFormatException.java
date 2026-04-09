package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidDateFormatException extends BaseAppException{
    public InvalidDateFormatException(String term) {
        super("Invalid "+ term + " date format.",
                HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_DATE_FORMAT);
    }
}