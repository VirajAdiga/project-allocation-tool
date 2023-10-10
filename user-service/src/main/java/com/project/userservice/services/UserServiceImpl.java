package com.project.userservice.services;

import com.project.userservice.dto.PublicUser;
import com.project.userservice.dto.UpdateUserRequest;
import com.project.userservice.entities.enums.Role;
import com.project.userservice.exception.DatabaseAccessException;
import com.project.userservice.exception.ResourceNotFoundException;
import com.project.userservice.entities.*;
import com.project.userservice.exception.UnauthorizedAccessException;
import com.project.userservice.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    // Retrieve a user by their email.
    @Override
    public User getUser(String email) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isEmpty()) {
                throw new ResourceNotFoundException("User not found with email: " + email);
            }
            return userOptional.get();
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
    }

    // Delete a user by their userId.
    @Override
    public void deleteUser(Integer userId) {
        try {
            // Retrieve the user by userId
            Optional<User> optionalUser = userRepository.findById(Long.valueOf(userId));

            // Check if the user exists
            if (optionalUser.isPresent()) {
                // Delete the user
                userRepository.delete(optionalUser.get());
            } else {
                throw new ResourceNotFoundException("User not found with id: " + userId);
            }
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
    }

    // Update a user's information by userId.
    @Override
    public User updateUser(Integer userId, User updatedUser) {
        try {
            Optional<User> optionalUser = userRepository.findById(Long.valueOf(userId));

            if (optionalUser.isPresent()) {
                User existingUser = optionalUser.get();

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
                // Save the updated user
                return userRepository.save(existingUser);
            } else {
                throw new ResourceNotFoundException("User not found with id: " + userId);
            }
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
    }

    // Retrieve a paginated list of all users.
    @Override
    public Page<User> getAllUsers(Integer pageSize, Integer pageNumber) {
        if (pageSize == null) pageSize = 1000;
        if (pageNumber == null) pageNumber = 0;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        try {
            return userRepository.findAll(pageRequest);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
    }

    @Override
    public User getUserById(Long userId) {
        try {
            return userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userId));
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
    }

    @Override
    public User createUser(User newUser) {
        try {
            return userRepository.save(newUser);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
    }

    @Override
    public List<User> getAllInterviewers() {
        try {
            return userRepository.findByIsInterviewerTrue();
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
    }

    @Override
    public void partialUpdateUser(Integer userId, UpdateUserRequest updateUserRequest) {
        try {
            User existingUser = userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

            if (updateUserRequest.getName() != null) {
                existingUser.setName(updateUserRequest.getName());
            }
            if (updateUserRequest.getEmail() != null) {
                existingUser.setEmail(updateUserRequest.getEmail());
            }
            if (updateUserRequest.getSkillIds() != null) {
                existingUser.setSkillIds(updateUserRequest.getSkillIds());
            }

            userRepository.save(existingUser);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new RuntimeException("Something went wrong, try again");
        }
    }

    @Override
    public void partialUpdateAdminUser(Integer userId, PublicUser publicUser) {
        try {
            User existingUser = userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
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
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new RuntimeException("Something went wrong, try again");
        }
    }

    @Override
    public void updateUserProjectAllocation(Long userId, Long userProjectId) {
        try {
            User existingUser = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
            existingUser.setProjectAllocatedId(userProjectId);
            userRepository.save(existingUser);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
    }

    /**
     * Retrieve a page of users from the free pool.
     *
     * @param pageSize   The maximum number of users per page.
     * @param pageNumber The page number to retrieve (0-based).
     * @return A page of free pool users.
     */
    public Page<User> getFreePoolUsers(Integer pageSize, Integer pageNumber, User requestUser) {
        if (pageSize == null) pageSize = 1000;
        if (pageNumber == null) pageNumber = 0;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        if (requestUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedAccessException("You don't have permission to access any reports.");
        }
        try {
            return userRepository.getFreePoolUsers(pageable);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
    }

    /**
     * Retrieve a page of users allocated to projects.
     *
     * @param pageSize   The maximum number of users per page.
     * @param pageNumber The page number to retrieve (0-based).
     * @return A page of allocated users within the specified date range.
     */
    public Page<User> getAllAllocatedUsers(Integer pageSize, Integer pageNumber, User requestUser) {
        if (pageSize == null) pageSize = 1000;
        if (pageNumber == null) pageNumber = 0;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        if (requestUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedAccessException("You don't have permission to access any reports.");
        }
        try {
            return userRepository.getAllAllocatedUsers(pageable);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
    }
}
