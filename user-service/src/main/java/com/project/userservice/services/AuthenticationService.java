package com.project.userservice.services;

import com.project.userservice.dto.RegisterRequest;
import com.project.userservice.dto.AuthenticationRequest;
import com.project.userservice.exception.CustomAuthenticationException;
import com.project.userservice.exception.DatabaseAccessException;
import com.project.userservice.exception.ResourceNotFoundException;
import com.project.userservice.exception.UserRegistrationException;
import com.project.userservice.entities.User;
import com.project.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class responsible for user authentication, registration, and logout.
 */
@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
                .skillIds(null) // Skills will be set later
                .build();

        // Save the user to the repository and return the saved user.
        try {
            return userRepository.save(user);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
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
        User user;
        try {
            user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }

        // Compare the provided password with the encoded password in the user object.
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return user; // Return the user if authentication is successful.
        }
        // Throw an exception if authentication fails.
        throw new CustomAuthenticationException("Invalid credentials");
    }
}
