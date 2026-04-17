package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;
import com.cinema.model.enums.TicketStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public abstract class BaseAppException extends RuntimeException {

    private Long entityId;
    private TicketStatus ticketStatus;
    private final HttpStatus status;
    private final ErrorCode errorCode;

    protected BaseAppException(
            String message,
            HttpStatus status,
            ErrorCode errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

}
