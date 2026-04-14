package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class SeatScreeningMismatchException extends BaseAppException{
    public SeatScreeningMismatchException(Long id) {
        super("Seat "+ id + " does not belongs to this hall.",
                HttpStatus.BAD_REQUEST,
                ErrorCode.SEAT_BELONGS_TO_ANOTHER_HALL);
    }
}