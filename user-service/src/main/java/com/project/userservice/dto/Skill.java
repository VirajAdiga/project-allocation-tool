package com.project.userservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.userservice.dto.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
// Model class representing a skill.
public class Skill {
    private Long id;         // Unique identifier for the skill.
    private String title;    // Title/name of the skill.
    @JsonIgnore
    private List<User> users; // List of users associated with this skill (ignored during JSON serialization).
}
