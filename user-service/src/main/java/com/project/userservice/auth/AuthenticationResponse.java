package com.project.userservice.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Represents an authentication response containing a JWT token and an optional message.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    /**
     * The JWT token generated upon successful authentication.
     */
    private String token;

    /**
     * An optional message associated with the authentication response.
     */
    private String message;
}
