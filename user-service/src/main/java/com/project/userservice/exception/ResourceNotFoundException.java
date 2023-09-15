package com.project.userservice.exception;

/**
 * Custom exception class for handling resource not found errors.
 * This exception is thrown when a requested resource is not found in the system.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor to create an instance of the exception with a custom error message.
     *
     * @param message The detailed error message describing the resource not found situation.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}


