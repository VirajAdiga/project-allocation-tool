package com.project.userservice.auth;

import com.project.userservice.models.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Represents a registration request containing user details for registration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    /**
     * The name of the user being registered.
     */
    private String name;

    /**
     * The email address of the user being registered.
     */
    private String email;

    /**
     * The password for the user being registered.
     */
    private String password;

    /**
     * The role of the user being registered.
     */
    @Enumerated(EnumType.STRING)
    private Role role;
}
