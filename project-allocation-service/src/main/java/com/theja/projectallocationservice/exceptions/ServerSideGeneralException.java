package com.theja.projectallocationservice.exceptions;

//General internal server side exception
public class ServerSideGeneralException extends RuntimeException{

    public ServerSideGeneralException(String message){
        super(message);
    }
}
