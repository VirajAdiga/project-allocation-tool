package com.theja.projectallocationservice.models;

/**
 * Enum representing the possible status values of an interview.
 */
public enum InterviewStatus {
    SCHEDULED,   // Interview is scheduled but not yet conducted
    COMPLETED,   // Interview has been successfully completed
    CANCELLED    // Interview has been cancelled before completion
}
