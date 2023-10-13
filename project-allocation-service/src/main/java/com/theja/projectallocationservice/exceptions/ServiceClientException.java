package com.theja.projectallocationservice.exceptions;

// Exception class to handle inter service communication exceptions
public class ServiceClientException extends RuntimeException{

    public ServiceClientException(String message){
        super(message);
    }
}
