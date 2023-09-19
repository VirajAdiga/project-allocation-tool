package com.theja.projectallocationservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.theja.projectallocationservice.dto.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a user in the system with associated information and activities.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;                // Unique identifier for the user
    private String name;            // Name of the user
    private String password;        // Password of the user
    private String email;           // Email address of the user
    private List<Skill> skills;     // List of skills possessed by the user
    @JsonIgnore
    private List<Interview> interviews;   // List of interviews conducted by the user
    @JsonIgnore
    private List<Application> applications; // List of applications submitted by the user
    @JsonIgnore
    private List<AuditLog> actions;         // List of audit log entries related to the user's actions
    @JsonIgnore
    private List<Opening> openings;         // List of openings managed by the user
}
