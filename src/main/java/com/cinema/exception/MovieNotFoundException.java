package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class MovieNotFoundException extends BaseAppException {
    public MovieNotFoundException(Long id) {
        super("Movie not found: " + id,
                HttpStatus.NOT_FOUND,
                ErrorCode.MOVIE_NOT_FOUND);
    }
}