package com.theja.projectallocationservice.exceptions;

// Custom class to represent error responses with status code and message
public class CustomErrorResponse {

    // Private fields to store status code and error message
    private int status;
    private String message;

    // Constructor to create a custom error response with status code and message
    public CustomErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    // Getter method to retrieve the status code
    public int getStatus() {
        return status;
    }

    // Getter method to retrieve the error message
    public String getMessage() {
        return message;
    }
}

