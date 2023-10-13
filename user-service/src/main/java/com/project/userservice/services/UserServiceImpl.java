package com.project.userservice.services;

import com.project.userservice.dto.PublicUser;
import com.project.userservice.dto.UpdateUserRequest;
import com.project.userservice.entities.enums.Role;
import com.project.userservice.exception.DatabaseAccessException;
import com.project.userservice.exception.ResourceNotFoundException;
import com.project.userservice.entities.*;
import com.project.userservice.exception.ServerSideGeneralException;
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
        Optional<User> userOptional;
        try {
            userOptional = userRepository.findByEmail(email);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }

        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }
        return userOptional.get();
    }

    // Delete a user by their userId.
    @Override
    public void deleteUser(Integer userId) {
        Optional<User> optionalUser;
        try {
            // Retrieve the user by userId
            optionalUser = userRepository.findById(Long.valueOf(userId));
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
        // Check if the user exists
        if (optionalUser.isPresent()) {
            // Delete the user
            userRepository.delete(optionalUser.get());
        } else {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
    }

    // Update a user's information by userId.
    @Override
    public User updateUser(Integer userId, User updatedUser) {
        Optional<User> optionalUser;
        try {
            optionalUser = userRepository.findById(Long.valueOf(userId));
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
        if (optionalUser.isEmpty()){
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
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
        try {
            return userRepository.save(existingUser);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
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
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }

    @Override
    public User getUserById(Long userId) {
        Optional<User> user;
        try {
            user = userRepository.findById(userId);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }

        if (user.isEmpty()){
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return user.get();
    }

    @Override
    public User createUser(User newUser) {
        try {
            return userRepository.save(newUser);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
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
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }

    @Override
    public void partialUpdateUser(Integer userId, UpdateUserRequest updateUserRequest) {
        Optional<User> existingUser;
        try {
            existingUser = userRepository.findById(Long.valueOf(userId));
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new RuntimeException("Something went wrong!");
        }

        if (existingUser.isEmpty()) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        User user = existingUser.get();
        if (updateUserRequest.getName() != null) {
            user.setName(updateUserRequest.getName());
        }
        if (updateUserRequest.getEmail() != null) {
            user.setEmail(updateUserRequest.getEmail());
        }
        if (updateUserRequest.getSkillIds() != null) {
            user.setSkillIds(updateUserRequest.getSkillIds());
        }
        try{
            userRepository.save(user);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new RuntimeException("Something went wrong!");
        }
    }

    @Override
    public void partialUpdateAdminUser(Integer userId, PublicUser publicUser) {
        Optional<User> existingUser;
        try {
            existingUser = userRepository.findById(Long.valueOf(userId));
        } catch (DataAccessException exception) {
            throw new DatabaseAccessException("Error accessing the database");
        } catch (Exception exception) {
            throw new RuntimeException("Something went wrong!");
        }
        if (existingUser.isEmpty()) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        User user = existingUser.get();
        if (publicUser.getName() != null) {
            user.setName(publicUser.getName());
        }
        if (publicUser.getEmail() != null) {
            user.setEmail(publicUser.getEmail());
        }
        if (publicUser.getRole() != null) {
            user.setRole(publicUser.getRole());
        }
        if (publicUser.isInterviewer() != user.isInterviewer()) {
            user.setInterviewer(publicUser.isInterviewer());
        }
        try {
            userRepository.save(user);
        } catch (DataAccessException exception) {
            throw new DatabaseAccessException("Error accessing the database");
        } catch (Exception exception) {
            throw new RuntimeException("Something went wrong!");
        }
    }

    @Override
    public void updateUserProjectAllocation(Long userId, Long userProjectId) {
        Optional<User> existingUser;
        try {
            existingUser = userRepository.findById(userId);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
        if (existingUser.isEmpty()) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        User user = existingUser.get();
        user.setProjectAllocatedId(userProjectId);
        try{
            userRepository.save(user);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
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
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
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
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }
}
