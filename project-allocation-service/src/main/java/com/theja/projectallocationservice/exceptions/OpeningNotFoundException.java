package com.theja.projectallocationservice.exceptions;

// Custom exception class to represent the scenario when an opening is not found
public class OpeningNotFoundException extends ResourceNotFoundException {

    // Constructor that takes the opening ID and creates an error message
    public OpeningNotFoundException(Long id) {
        super("Opening not found with ID: " + id);
    }
}

