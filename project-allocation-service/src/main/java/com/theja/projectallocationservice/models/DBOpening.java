package com.theja.projectallocationservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a database entity for storing job openings.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "openings")
public class DBOpening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Unique identifier for the opening

    @NotNull
    private String title;  // Title of the job opening

    @NotNull
    private String details;  // Details or description of the job opening

    @NotNull(message = "Experience level is required for an opening")
    private Integer level;  // Experience level required for the job

    @NotNull
    private String location;  // Location of the job opening

    @Enumerated(EnumType.STRING)
    private OpeningStatus status;  // Status of the job opening, e.g., "ACTIVE," "CLOSED"

    @ManyToOne
    @JoinColumn(name = "created_by")
    private DBUser recruiter;  // The user who created the job opening

    @ManyToOne(optional = false)
    @JoinColumn(name="project_id")
    private DBProject project;  // The project associated with the job opening

    @OneToMany(mappedBy="opening", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DBApplication> applications = new ArrayList<>();  // List of applications for this opening

    @ManyToMany
    @JoinTable(name = "openings_skills",
            joinColumns = @JoinColumn(name = "opening_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    @NotNull
    private List<DBSkill> skills;  // List of skills required for the job opening
}
