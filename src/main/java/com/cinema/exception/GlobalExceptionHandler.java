package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ErrorResponse> handlePayment(PaymentException ex) {

        ErrorResponse response = new ErrorResponse(
                ErrorCode.PAYMENT_FAILED,
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(Exception ex) {
        ErrorResponse response = new ErrorResponse(
                ErrorCode.INTERNAL_SERVER_ERROR,
                "Unexpected server error",
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @ExceptionHandler(BaseAppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(BaseAppException ex) {
        ErrorResponse response = new ErrorResponse(
                ex.getErrorCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(ex.getStatus())
                .body(response);
    }

}
