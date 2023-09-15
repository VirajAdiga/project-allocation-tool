package com.theja.projectallocationservice.exceptions;


// Custom exception class to represent the scenario when an opening is not found
public class InterviewNotFoundException extends ResourceNotFoundException {

    // Constructor that takes the opening ID and creates an error message
    public InterviewNotFoundException(String message) {
        super(message);
    }
}