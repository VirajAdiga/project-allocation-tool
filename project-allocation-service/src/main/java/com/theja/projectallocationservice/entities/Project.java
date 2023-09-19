package com.theja.projectallocationservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * Represents a database entity for storing project details.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Unique identifier for the project

    private String title;  // Title of the project

    private String details;  // Details or description of the project

    @ManyToMany
    @JoinTable(name = "users_projects",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> allocatedUsers;  // Set of users allocated to the project

    @OneToMany(mappedBy="project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Opening> openings;  // List of job openings associated with the project
}
