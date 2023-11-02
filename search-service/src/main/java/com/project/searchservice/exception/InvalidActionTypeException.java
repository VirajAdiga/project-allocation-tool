package com.project.searchservice.exception;

public class InvalidActionTypeException extends RuntimeException{
    public InvalidActionTypeException(String message){
        super(message);
    }
}
