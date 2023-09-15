package com.project.userservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
// Entity class representing a user in the database.
public class DBUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Unique identifier for the user.
    @NotEmpty(message = "Name is required")
    private String name; // Name of the user.
    @NotEmpty(message = "Name is required")
    @Email(message = "Invalid email format")
    private String email; // Email address of the user.
    @NotEmpty(message = "Password is required")
    private String password; // Password of the user.
    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    private Role role; // Role of the user.
    private String token; // Authentication token for the user.
    @Column(name = "is_interviewer")
    private boolean isInterviewer; // Corresponds to the can_interview column in the database.
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(
            name = "users_skills",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<DBSkill> skills; // List of skills associated with the user.
}
