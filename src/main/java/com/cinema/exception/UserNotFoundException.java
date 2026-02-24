package com.cinema.exception;

public class UserNotFoundException extends BaseAppException{
    public UserNotFoundException(String username){
        super("User not found: " + username + ".");
    }
}
