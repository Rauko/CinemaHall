package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class SeatAlreadyTakenException extends BaseAppException{
    public SeatAlreadyTakenException(Long seatId){
        super("This seat has been already taken: " + seatId,
                HttpStatus.CONFLICT,
                ErrorCode.SEAT_ALREADY_TAKEN);
    }
}
