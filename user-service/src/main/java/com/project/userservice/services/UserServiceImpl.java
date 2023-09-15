package com.project.userservice.services;

import com.project.userservice.exception.ResourceNotFoundException;
import com.project.userservice.models.*;
import com.project.userservice.repositories.SkillRepository;
import com.project.userservice.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Service implementation for user-related operations.
@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillRepository skillRepository;

    // Retrieve a user by their email.
    @Override
    public Optional<DBUser> getUser(String email) {
        log.info("Fetching user {}", email);
        Optional<DBUser> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }
        return userOptional;
    }

    // Delete a user by their userId.
    @Override
    public void deleteUser(Integer userId) {
        // Retrieve the user by userId
        Optional<DBUser> optionalUser = userRepository.findById(userId);

        // Check if the user exists
        if (optionalUser.isPresent()) {
            // Delete the user
            DBUser user = optionalUser.get();
            userRepository.delete(user);
        } else {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
    }

    // Update a user's information by userId.
    @Override
    public DBUser updateUser(Integer userId, DBUser updatedUser) {
        Optional<DBUser> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            DBUser existingUser = optionalUser.get();

            // Apply partial updates only for the fields that are provided in updatedUser
            if (updatedUser.getName() != null) {
                existingUser.setName(updatedUser.getName());
            }
            if (updatedUser.getEmail() != null) {
                existingUser.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getPassword() != null) {
                existingUser.setPassword(updatedUser.getPassword());
            }
            if (updatedUser.getRole() != null) {
                existingUser.setRole(updatedUser.getRole());
            }
            if (updatedUser.getToken() != null) {
                existingUser.setToken(updatedUser.getToken());
            }

            System.out.println(existingUser);

            // Save the updated user
            return userRepository.save(existingUser);
        } else {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
    }

    // Retrieve a paginated list of all users.
    @Override
    public Page<DBUser> getAllUsers(Integer pageSize, Integer pageNumber) {
        if (pageSize == null) pageSize = 1000;
        if (pageNumber == null) pageNumber = 0;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        System.out.println(pageRequest);
        return userRepository.findAll(pageRequest);
    }

    @Override
    public Optional<DBUser> getUserById(Integer userId) {
        return userRepository.findById(userId);
    }

    @Override
    public DBUser createUser(DBUser newUser) {
        return userRepository.save(newUser);
    }

    @Override
    public List<DBUser> getAllInterviewers() {
        return userRepository.findByIsInterviewerTrue();
    }

    @Override
    public void partialUpdateUser(Integer userId, UpdateUserRequest updateUserRequest) {
        DBUser existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (updateUserRequest.getName() != null) {
            existingUser.setName(updateUserRequest.getName());
        }
        if (updateUserRequest.getEmail() != null) {
            existingUser.setEmail(updateUserRequest.getEmail());
        }
        if (updateUserRequest.getSkills() != null) {
            existingUser.setSkills(updateUserRequest.getSkills());
        }

        userRepository.save(existingUser);
    }

    @Override
    public void partialUpdateAdminUser(Integer userId, PublicUser publicUser) {
        DBUser existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (publicUser.getName() != null) {
            existingUser.setName(publicUser.getName());
        }
        if (publicUser.getEmail() != null) {
            existingUser.setEmail(publicUser.getEmail());
        }
        if (publicUser.getRole() != null) {
            existingUser.setRole(publicUser.getRole());
        }
        if (publicUser.isInterviewer() != existingUser.isInterviewer()) {
            existingUser.setInterviewer(publicUser.isInterviewer());
        }
        userRepository.save(existingUser);
    }
}
