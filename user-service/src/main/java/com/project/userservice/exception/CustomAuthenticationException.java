package com.project.userservice.exception;

/**
 * Custom exception class for handling authentication-related errors.
 */
public class CustomAuthenticationException extends RuntimeException {

    /**
     * Constructor to create an instance of the exception with a custom error message.
     *
     * @param message The error message for the exception.
     */
    public CustomAuthenticationException(String message) {
        super(message);
    }
}

