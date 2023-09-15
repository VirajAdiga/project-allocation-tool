package com.theja.projectallocationservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for creating a new project.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectDTO {
    private String title;   // Title of the new project
    private String details; // Details about the new project
}
