package com.project.userservice.exception;

// Exception class to handle inter service communication exceptions
public class ServiceClientException extends RuntimeException{

    public ServiceClientException(String message){
        super(message);
    }
}
