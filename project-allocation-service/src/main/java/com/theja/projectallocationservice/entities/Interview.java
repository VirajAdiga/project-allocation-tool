package com.theja.projectallocationservice.entities;

import com.theja.projectallocationservice.entities.enums.InterviewStatus;
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
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Unique identifier for the interview

    private String title;  // Title of the interview

    @Column(nullable = false)
    private Long interviewerId;  // The user who conducts the interview

    @Enumerated(EnumType.STRING)
    private InterviewStatus status;  // Status of the interview, e.g., "SCHEDULED," "COMPLETED"

    private String feedback;

    private Date scheduledTime;  // Scheduled date and time of the interview

    @ManyToOne(optional = false)
    @JoinColumn(name = "application_id")
    private Application application;  // The application associated with the interview
}
