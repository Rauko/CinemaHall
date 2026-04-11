package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class SeatNotFoundException extends BaseAppException{
    public SeatNotFoundException(Long seatId){
        super("Seat not found: " + seatId,
                HttpStatus.NOT_FOUND,
                ErrorCode.SEAT_NOT_FOUND);
    }
}