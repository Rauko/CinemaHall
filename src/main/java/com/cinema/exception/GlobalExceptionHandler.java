package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ErrorResponse> handlePayment(PaymentException ex) {

        log.error("Payment failed: errorMessage={}, id={}",
                ex.getMessage(),
                extractEntityID(ex)
        );

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

        log.error("Unexpected exception: errorMessage={}, id={}",
                ex.getMessage(),
                extractEntityID(ex),
                ex
        );

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

        log.warn("App exception occurred: errorCode={}, errorMessage={}, id={}",
                ex.getErrorCode(),
                ex.getMessage(),
                extractEntityID(ex)
        );

        ErrorResponse response = new ErrorResponse(
                ex.getErrorCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(ex.getStatus())
                .body(response);
    }

    private Object extractEntityID(Exception ex) {
        if(ex instanceof BaseAppException baseEx) {
            return baseEx.getEntityId();
        }
        return null;
    }

}
