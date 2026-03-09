package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;

import java.time.LocalDateTime;

public record ErrorResponse(
        ErrorCode errorCode,
        String error,
        String message,
        LocalDateTime timestamp
) {}
