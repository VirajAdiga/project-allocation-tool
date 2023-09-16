package com.project.userservice.dto;

import com.project.userservice.entities.enums.Role;
import lombok.Data;

@Data
// Data class representing a request to create an admin user.
public class AdminUserRequest {
    private String name; // Name of the admin user.
    private String email; // Email address of the admin user.
    private Role role; // Role of the admin user.
}
