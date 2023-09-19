package com.project.userservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "skills")
// Entity class representing a skill in the database.
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for the skill.
    private String title; // Title/name of the skill.

    @ManyToMany(mappedBy = "skills")
    private List<User> users; // List of users associated with this skill.
}
