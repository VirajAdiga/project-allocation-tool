package com.theja.projectallocationservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a database entity for storing skill information.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "skills")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Unique identifier for the skill

    private String title;  // Title or name of the skill

    @ManyToMany(mappedBy = "skills")
    private List<Opening> openings;  // List of job openings requiring this skill
}
