package com.project.userservice.controllers;

import com.project.userservice.dto.PublicUser;
import com.project.userservice.dto.PublicUserListResponse;
import com.project.userservice.dto.UpdateUserRequest;
import com.project.userservice.exception.ResourceNotFoundException;
import com.project.userservice.mappers.UserMapper;
import com.project.userservice.entities.*;
import com.project.userservice.services.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * Controller class responsible for user management and related endpoints.
 */
@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
@Tag(name = "Users", description = "Endpoints related to user management")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    /**
     * Gets a user by their userId.
     *
     * @param userId The ID of the user to be retrieved.
     * @return ResponseEntity indicating the result of the get operation.
     */
    @GetMapping("/public/{userId}")
    @Hidden
    public PublicUser getUser(@PathVariable Long userId) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isPresent()){
            return userMapper.entityToPublicModel(user.get());
        }else{
            throw new ResourceNotFoundException(userId.toString());
        }
    }

    /**
     * Gets list of users by their userId.
     *
     * @param requestBody The request body containing id of users.
     * @return ResponseEntity indicating the result of the get operation.
     */
    @PostMapping("/public")
    @Hidden
    public PublicUserListResponse getUsers(@RequestBody Map<String, List<Long>> requestBody) {
        List<Long> userIds = requestBody.get("userIds");
        List<PublicUser> publicUsers = new ArrayList<>();
        for (Long userId: userIds ) {
            Optional<User> user = userService.getUserById(userId);
            if (user.isPresent()) {
                PublicUser publicUser = userMapper.entityToPublicModel(user.get());
                publicUsers.add(publicUser);
            }
        }
        return new PublicUserListResponse(publicUsers, (long) publicUsers.size());
    }

    /**
     * Creates a new user with the provided user data.
     *
     * @param newUser The user data to create a new user.
     * @return ResponseEntity containing the created user's public representation.
     */
    @PostMapping
    @Hidden
    public ResponseEntity<PublicUser> createUser(@RequestBody User newUser) {
        User createdUser = userService.createUser(newUser);
        return ResponseEntity.ok(userMapper.entityToPublicModel(createdUser));
    }

    /**
     * Deletes a user by their userId.
     *
     * @param userId The ID of the user to be deleted.
     * @return ResponseEntity indicating the result of the delete operation.
     */
    @DeleteMapping("/{userId}")
    @Hidden
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId) {
        try {
            // Call the UserService to delete the user by userId
            userService.deleteUser(userId);

            // Return a success response
            return ResponseEntity.ok("User deleted successfully");
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Cannot delete this user. This user is scheduled to conduct interviews.");
        }
    }

    /**
     * Retrieves a paginated list of users with optional pageSize and pageNumber parameters.
     *
     * @param pageSize   The number of items per page.
     * @param pageNumber The page number.
     * @return ResponseEntity containing the paginated list of users.
     */
    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a paginated list of users with optional pageSize and pageNumber parameters")
    @ApiResponse(responseCode = "200", description = "List of users retrieved successfully", content = @Content(schema = @Schema(implementation = PublicUserListResponse.class)))
    public ResponseEntity<PublicUserListResponse> getAllUsers(@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageNumber) {
        Page<User> dbUsers = userService.getAllUsers(pageSize, pageNumber);
        PublicUserListResponse response = PublicUserListResponse.builder()
                .users(userMapper.entityToPublicModel(dbUsers.getContent()))
                .totalElements(dbUsers.getTotalElements())
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a list of all users without pagination, specifically for interviewers.
     *
     * @return ResponseEntity containing the list of interviewers.
     */
    @GetMapping("/interviewers")
    @Operation(summary = "Get interviewers", description = "Retrieve a list of all users without pagination, specifically for interviewers")
    @ApiResponse(responseCode = "200", description = "List of interviewers retrieved successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PublicUser.class))))
    public ResponseEntity<List<PublicUser>> getAllInterviewers() {
        List<User> users = userService.getAllInterviewers();
        return ResponseEntity.ok(userMapper.entityToPublicModel(users));
    }

    /**
     * Partially updates an admin user's details by their userId.
     *
     * @param userId      The ID of the admin user to be updated.
     * @param publicUser  The updated user data.
     * @return ResponseEntity indicating the result of the partial update operation.
     */
    @PatchMapping("/{userId}/admin")
    @Hidden
    public ResponseEntity<?> partialUpdateAdminUser(
            @PathVariable Integer userId,
            @RequestBody PublicUser publicUser) {
        try {
            userService.partialUpdateAdminUser(userId, publicUser);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Partially updates a user's details by their userId.
     *
     * @param userId           The ID of the user to be updated.
     * @param updateUserRequest The updated user data.
     * @return ResponseEntity indicating the result of the partial update operation.
     */
    @PatchMapping("/{userId}")
    @Hidden
    public ResponseEntity<?> partialUpdateUser(
            @PathVariable Integer userId,
            @RequestBody UpdateUserRequest updateUserRequest) {
        try {
            userService.partialUpdateUser(userId, updateUserRequest);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
