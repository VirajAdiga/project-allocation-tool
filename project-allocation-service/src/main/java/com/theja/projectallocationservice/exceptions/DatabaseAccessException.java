package com.theja.projectallocationservice.exceptions;

/**
 * Custom exception class for handling database access errors.
 * This exception is thrown when there is problem with database access.
 */
public class DatabaseAccessException  extends RuntimeException {

    public DatabaseAccessException(String message){
        super(message);
    }
}
