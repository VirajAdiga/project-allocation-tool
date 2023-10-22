package com.project.emailservice.exception;

public class SendMailException extends RuntimeException{

    public SendMailException(String message){
        super(message);
    }
}
