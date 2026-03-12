package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class ReservationExpiredException extends BaseAppException {
    public ReservationExpiredException() {
        super("Reservation expired",
                HttpStatus.CONFLICT,
                ErrorCode.RESERVATION_EXPIRED);
    }
}
