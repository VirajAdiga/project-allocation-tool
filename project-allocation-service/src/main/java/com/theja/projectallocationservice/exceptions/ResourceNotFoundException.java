package com.theja.projectallocationservice.exceptions;

// Custom exception class to represent the scenario when a requested resource is not found
public class ResourceNotFoundException extends RuntimeException {

    // Constructor that takes an error message and passes it to the parent class constructor
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

