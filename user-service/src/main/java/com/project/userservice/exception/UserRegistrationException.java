package com.project.userservice.exception;

/**
 * Custom exception class for handling user registration-related errors.
 * This exception is thrown when an error occurs during user registration.
 */
public class UserRegistrationException extends RuntimeException {

    /**
     * Constructor to create an instance of the exception with a custom error message.
     *
     * @param message The detailed error message describing the user registration error.
     */
    public UserRegistrationException(String message) {
        super(message);
    }
}


