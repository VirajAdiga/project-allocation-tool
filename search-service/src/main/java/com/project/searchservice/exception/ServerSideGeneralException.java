package com.project.searchservice.exception;

//General internal server side exception
public class ServerSideGeneralException extends RuntimeException{
    public ServerSideGeneralException(String message){
        super(message);
    }
}
