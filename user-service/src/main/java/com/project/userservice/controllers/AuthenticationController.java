package com.project.userservice.controllers;

import com.project.userservice.dto.AuthenticationRequest;
import com.project.userservice.services.AuthenticationService;
import com.project.userservice.services.AuthorizationService;
import com.project.userservice.dto.RegisterRequest;
import com.project.userservice.entities.User;
import com.project.userservice.services.BlacklistTokenService;
import com.project.userservice.services.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class responsible for handling user authentication and authorization endpoints.
 */
@RestController
@RequestMapping("/api/v1/authentication")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints related to user authentication and authorization")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private UserService userService;

    @Autowired
    private BlacklistTokenService blacklistTokenService;

    /**
     * Registers a new user.
     *
     * @param request The registration request containing user details.
     * @return ResponseEntity with the generated JWT token upon successful registration.
     */
    @PostMapping("/register")
    @Hidden
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request
    ) {
        User createdUser = authenticationService.register(request);

        // Generate the JWT token for the created user
        String token = authorizationService.generateJWTTokenForUser(createdUser);

        // Save the updated user with the token
        userService.updateUser(createdUser.getId(), createdUser);

        return ResponseEntity.ok(token);
    }

    /**
     * Authenticates an existing user.
     *
     * @param request  The authentication request containing user credentials.
     * @param response HttpServletResponse to set the response headers.
     * @return ResponseEntity with the generated JWT token upon successful authentication.
     */
    @PostMapping("/authenticate")
    @Operation(summary = "Authenticate user", description = "Authenticate an existing user using their credentials")
    @ApiResponse(responseCode = "200", description = "User authenticated successfully and token generated")
    @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials")
    public ResponseEntity<String> authenticate(
            @RequestBody AuthenticationRequest request, HttpServletResponse response
    ) {
        User existingUser = authenticationService.authenticate(request);

        // Generate the JWT token for the created user
        String token = authorizationService.generateJWTTokenForUser(existingUser);

        // Save the updated user with the token
        userService.updateUser(existingUser.getId(), existingUser);

        return ResponseEntity.ok(token);
    }

    /**
     * Logs out a user.
     *
     * @param request  HttpServletRequest to perform the logout.
     * @param response HttpServletResponse to set the response headers.
     * @return ResponseEntity indicating the result of the logout operation.
     */
    @PostMapping("/logout")
    @Hidden
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Call the logout service method to perform the logout logic
            blacklistTokenService.addTokenToBlacklist(request, response);
            return ResponseEntity.ok("Logged out successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to logout.");
        }
    }
}
