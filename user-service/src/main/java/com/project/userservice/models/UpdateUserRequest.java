package com.project.userservice.models;

import lombok.Data;

import java.util.List;

@Data
public class UpdateUserRequest {
    private String name;
    private String email;
    private List<DBSkill> skills; // Add this field for skills
}
