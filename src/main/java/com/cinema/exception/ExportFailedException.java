package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;
import com.cinema.model.enums.ExportFormat;
import org.springframework.http.HttpStatus;

public class ExportFailedException extends BaseAppException{
    public ExportFailedException(ExportFormat format) {
        super(format.toString() + " export failed.",
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorCode.EXPORT_FAILED);
    }
}