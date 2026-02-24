package com.cinema.exception;

public class AppAccessDeniedException extends BaseAppException{
    public AppAccessDeniedException(){
        super("Access denied.");
    }
}
