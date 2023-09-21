package com.project.userservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateUserRequest {
    private String name;
    private String email;
    private List<Long> skillIds; // Add this field for skills
}
