package com.project.userservice.dto;

import com.project.userservice.entities.Skill;
import lombok.Data;

import java.util.List;

@Data
public class UpdateUserRequest {
    private String name;
    private String email;
    private List<Skill> skills; // Add this field for skills
}
