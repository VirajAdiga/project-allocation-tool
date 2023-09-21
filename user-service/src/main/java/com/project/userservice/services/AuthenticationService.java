package com.project.userservice.services;

import com.project.userservice.dto.RegisterRequest;
import com.project.userservice.dto.AuthenticationRequest;
import com.project.userservice.exception.UserRegistrationException;
import com.project.userservice.entities.User;
import com.project.userservice.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class responsible for user authentication, registration, and logout.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserService userService;

    /**
     * Registers a new user based on the provided registration request.
     *
     * @param request The registration request containing user details.
     * @return The registered user with updated information.
     * @throws UserRegistrationException If the provided email is already registered.
     */
    public User register(RegisterRequest request) {
        // Check if user with the given email already exists
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new UserRegistrationException("This email address is already registered");
        }

        // Create a new DBUser instance with the provided registration details.
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .isInterviewer(false)
                .token(null) // Token will be set later
                .skillIds(null) // Skills will be set later
                .build();

        // Save the user to the repository and return the saved user.
        return userRepository.save(user);
    }

    /**
     * Authenticates a user based on the provided authentication request.
     *
     * @param request The authentication request containing user credentials.
     * @return The authenticated user if successful.
     * @throws RuntimeException If authentication fails.
     */
    public User authenticate(AuthenticationRequest request) {
        // Retrieve the user based on the provided email from the repository.
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        // Compare the provided password with the encoded password in the user object.
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return user; // Return the user if authentication is successful.
        }
        // Throw an exception if authentication fails.
        throw new RuntimeException("Invalid credentials");
    }

    /**
     * Logs out a user by updating their token to null.
     *
     * @param request  The HttpServletRequest from the user's request.
     * @param response The HttpServletResponse to modify the response headers.
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Extract the JWT token from the Authorization header.
        jwt = authHeader.substring(7);

        // Extract the user's email from the JWT token using the JwtService.
        userEmail = jwtService.extractSubject(jwt);
        if (userEmail != null) {
            // Fetch the user from the database using the email
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

            // Update the user's token to null to perform logout.
            user.setToken(null);
            userService.updateUser(user.getId(), user);
        }

        // Clear the Authorization header in the response to log the user out.
        response.setHeader("Authorization", "");
    }
}
