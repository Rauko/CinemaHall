package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class RoleChangeNotAllowedException extends BaseAppException {
    public RoleChangeNotAllowedException(Long id) {
        super("Role for user " + id + " cannot be changed.",
                HttpStatus.NOT_FOUND,
                ErrorCode.TICKET_NOT_FOUND);
    }
}