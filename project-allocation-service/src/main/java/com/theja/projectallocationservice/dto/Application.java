package com.theja.projectallocationservice.dto;

import com.theja.projectallocationservice.entities.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Application {
    private Long id; // Unique identifier for the application
    private ApplicationStatus status; // Status of the application (e.g., PENDING, APPROVED, REJECTED)
    private Date appliedAt; // Date when the application was submitted
    private PublicUser candidate; // Public information about the candidate who applied
    private Opening opening; // Details of the opening to which the application is related
    private List<Interview> interviews; // List of interviews associated with the application
}
