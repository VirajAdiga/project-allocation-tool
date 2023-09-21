package com.project.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// Model class representing a skill.
public class Skill {
    private Long id;         // Unique identifier for the skill.
    private String title;    // Title/name of the skill.
}
