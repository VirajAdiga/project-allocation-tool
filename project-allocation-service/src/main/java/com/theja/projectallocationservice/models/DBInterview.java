package com.theja.projectallocationservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Represents a database entity for storing interview details.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "interviews")
public class DBInterview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Unique identifier for the interview

    private String title;  // Title of the interview

    @ManyToOne(optional = false)
    @JoinColumn(name = "interviewer_id")
    private DBUser interviewer;  // The user who conducts the interview

    @Enumerated(EnumType.STRING)
    private InterviewStatus status;  // Status of the interview, e.g., "SCHEDULED," "COMPLETED"

    private String feedback;

    private Date scheduledTime;  // Scheduled date and time of the interview

    @ManyToOne(optional = false)
    @JoinColumn(name = "application_id")
    private DBApplication application;  // The application associated with the interview
}
