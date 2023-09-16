package com.project.userservice.services;

import com.project.userservice.dto.PublicUser;
import com.project.userservice.dto.UpdateUserRequest;
import com.project.userservice.entities.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * Service interface defining methods to manage user-related operations.
 */
public interface UserService {

    /**
     * Retrieves a user by their email.
     *
     * @param email The email of the user to retrieve.
     * @return An optional containing the user if found.
     */
    Optional<DBUser> getUser(String email);

    /**
     * Deletes a user by their userId.
     *
     * @param userId The ID of the user to delete.
     */
    void deleteUser(Integer userId);

    /**
     * Updates a user's information by userId.
     *
     * @param userId       The ID of the user to update.
     * @param updatedUser  The updated user data.
     * @return The updated user.
     */
    DBUser updateUser(Integer userId, DBUser updatedUser);

    /**
     * Retrieves a paginated list of all users.
     *
     * @param pageSize   The number of items per page.
     * @param pageNumber The page number.
     * @return A page containing the list of users.
     */
    Page<DBUser> getAllUsers(Integer pageSize, Integer pageNumber);

    /**
     * Retrieves a user by their userId.
     *
     * @param userId The ID of the user to retrieve.
     * @return An optional containing the user if found.
     */
    Optional<DBUser> getUserById(Integer userId);

    /**
     * Creates a new user.
     *
     * @param newUser The user data for creating a new user.
     * @return The created user.
     */
    DBUser createUser(DBUser newUser);

    /**
     * Retrieves a list of all users without pagination.
     *
     * @return A list containing all users.
     */
    List<DBUser> getAllInterviewers();

    /**
     * Partially updates a user's information by userId.
     *
     * @param userId            The ID of the user to update.
     * @param updateUserRequest The updated user data.
     */
    void partialUpdateUser(Integer userId, UpdateUserRequest updateUserRequest);

    /**
     * Partially updates an admin user's information by userId.
     *
     * @param userId      The ID of the admin user to update.
     * @param publicUser  The updated admin user data.
     */
    void partialUpdateAdminUser(Integer userId, PublicUser publicUser);
}
