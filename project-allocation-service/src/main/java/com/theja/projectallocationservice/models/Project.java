package com.theja.projectallocationservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

/**
 * Represents a project within the system.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    private Long id;                        // Unique identifier for the project
    private String title;                   // Title of the project
    private String details;                 // Details or description of the project
    private Collection<User> allocatedUsers; // Collection of users allocated to the project
    @JsonIgnore
    private List<Opening> openings;          // List of openings associated with the project
}
