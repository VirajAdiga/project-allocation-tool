package com.project.userservice.services;

import com.project.userservice.entities.DBUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Service class responsible for generating JWT tokens for user authorization.
 */
@Service
public class AuthorizationService {
    @Autowired
    private JwtService jwtService;

    /**
     * Generates a JWT token for the provided DBUser.
     *
     * @param user The user for whom the JWT token is generated.
     * @return The generated JWT token.
     */
    public String generateJWTTokenForUser(DBUser user) {
        // Generate a JWT token using the JwtService by passing an empty HashMap (claims)
        // and the user's email as the subject of the token.
        return jwtService.generateToken(new HashMap<>(), user.getEmail());
    }
}
