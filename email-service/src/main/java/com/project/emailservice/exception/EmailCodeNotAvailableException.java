package com.project.emailservice.exception;

public class EmailCodeNotAvailableException extends RuntimeException{

    public EmailCodeNotAvailableException(String message){
        super(message);
    }
}
