package com.project.userservice.exception;

// Custom exception class to represent unauthorized access to a resource or action
public class UnauthorizedAccessException extends RuntimeException {

    // Constructor that takes an error message and passes it to the parent class constructor
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
