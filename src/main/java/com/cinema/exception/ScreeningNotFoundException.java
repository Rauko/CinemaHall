package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class ScreeningNotFoundException extends BaseAppException{
    public ScreeningNotFoundException(Long screeningId){
        super("Screening not found: " + screeningId,
                HttpStatus.NOT_FOUND,
                ErrorCode.SCREENING_NOT_FOUND);
    }
}