package com.theja.projectallocationservice.exceptions;

// Custom exception class to handle cases where an application with a specific ID is not found
public class ApplicationNotFoundException extends ResourceNotFoundException {

    // Constructor that takes the ID of the application as a parameter
    public ApplicationNotFoundException(Long id) {
        // Call the constructor of the parent class (ResourceNotFoundException) with a custom error message
        super("Application not found with ID: " + id);
    }
}
