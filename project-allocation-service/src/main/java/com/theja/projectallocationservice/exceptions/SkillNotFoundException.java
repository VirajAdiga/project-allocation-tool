package com.theja.projectallocationservice.exceptions;

// Custom exception class to represent the scenario when a requested skill is not found
public class SkillNotFoundException extends ResourceNotFoundException {

    // Constructor that takes an error message and passes it to the parent class constructor
    public SkillNotFoundException(String message) {
        super(message);
    }
}
