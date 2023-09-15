package com.theja.projectallocationservice.exceptions;

// Custom exception class to represent the scenario when an opening update fails
public class OpeningUpdateException extends RuntimeException {

    // Constructor that takes an error message and passes it to the parent class constructor
    public OpeningUpdateException(String message) {
        super(message);
    }
}

