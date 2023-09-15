package com.theja.projectallocationservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents an opening for a project that can be filled by a candidate through an application.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Opening {
    private Long id;                   // Unique identifier for the opening
    private String title;              // Title of the opening
    private String details;            // Details/description of the opening
    private Integer level;             // Experience level required for the opening
    private String location;           // Location of the opening
    private OpeningStatus status;      // Status of the opening (e.g., open, closed)
    private PublicUser recruiter;      // User responsible for recruiting for the opening
    private Project project;           // Project to which the opening belongs
    private List<Application> applications;  // List of applications received for the opening
    private List<Skill> skills;         // List of skills required for the opening
}
