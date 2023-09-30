package com.theja.projectallocationservice.entities;

import com.theja.projectallocationservice.entities.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Represents a database entity for storing application-related information.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "applications")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Unique identifier for the application

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;  // Status of the application (APPLIED, REJECTED, ALLOCATED)

    @Column(name = "applied_at")
    private Date appliedAt;  // Date when the application was submitted

    @Column(nullable = false)
    private Long candidateId;  // The candidate (applicant) associated with the application

    @ManyToOne(optional = false)
    @JoinColumn(name="opening_id")
    private Opening opening;  // The opening (job position) associated with the application

    @OneToMany(mappedBy="application", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Interview> interviews;  // List of interviews associated with the application
}
