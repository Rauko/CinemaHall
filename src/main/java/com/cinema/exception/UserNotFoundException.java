package com.cinema.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseAppException{
    public UserNotFoundException(String username){
        super("User not found: " + username, HttpStatus.NOT_FOUND);
    }
}
