package com.theja.projectallocationservice.dto;

import com.theja.projectallocationservice.entities.enums.InterviewStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Represents an interview for an application.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Interview {
    private Long id;  // Interview ID
    private String title;  // Title of the interview
    private PublicUser interviewer;  // User who is the interviewer
    private InterviewStatus status;  // Status of the interview
    private String feedback;
    private Date scheduledTime;  // Scheduled time for the interview
    private Application application;  // Application associated with the interview
}
