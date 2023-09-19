package com.theja.projectallocationservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a database entity for storing user information.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Unique identifier for the user

    private String name;  // User's name

    private String password;  // User's password

    private String email;  // User's email

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "users_skills",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skills;  // List of skills possessed by the user

    @OneToMany(mappedBy="interviewer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Interview> interviews;  // List of interviews conducted by the user

    @OneToMany(mappedBy="candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;  // List of applications made by the user

    @OneToMany(mappedBy="user", cascade = CascadeType.ALL)
    private List<AuditLog> actions;  // List of audit logs associated with the user's actions

    @OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL)
    private List<Opening> openings;  // List of job openings managed by the user
}
