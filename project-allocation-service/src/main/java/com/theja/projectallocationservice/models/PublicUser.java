package com.theja.projectallocationservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a public view of a user, including basic information and skills.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicUser {
    private Long id;            // User's unique identifier
    private String name;        // User's name
    private String email;       // User's email
    private List<Skill> skills; // List of skills associated with the user
}
