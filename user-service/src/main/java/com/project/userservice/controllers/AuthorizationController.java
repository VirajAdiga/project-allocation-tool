package com.project.userservice.controllers;

import com.project.userservice.mappers.UserMapper;
import com.project.userservice.models.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class responsible for handling user authorization and permission-related endpoints.
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Authorization", description = "Endpoints related to user authorization and permissions")
public class AuthorizationController {
    @Autowired
    private UserMapper userMapper;

    /**
     * Retrieves a list of permissions associated with the authenticated user's role.
     *
     * @return List of permissions associated with the user's role.
     */
    @GetMapping("/authorization/permissions")
    @Operation(summary = "Get user permissions", description = "Retrieve a list of permissions associated with the authenticated user's role")
    @ApiResponse(responseCode = "200", description = "List of user permissions retrieved successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PermissionName.class))))
    public List<PermissionName> getPermissions() {
        DBUser user = (DBUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Determine the user's role and return the appropriate list of permissions.
        switch (user.getRole()) {
            case ADMIN:
                return List.of(PermissionName.CREATE_PROJECT, PermissionName.MANAGE_USERS, PermissionName.VIEW_USER_ACTIVITY, PermissionName.CREATE_OPENING, PermissionName.VIEW_PENDING_APPLICATIONS, PermissionName.VIEW_REPORTS, PermissionName.MANAGE_OPENINGS, PermissionName.REGISTER_USER, PermissionName.ADMIN_OWN_OPENINGS, PermissionName.ADMIN_OTHER_OPENINGS, PermissionName.MANAGE_INTERVIEWER_STATUS);
            case RECRUITER:
                return List.of(PermissionName.CREATE_OPENING, PermissionName.VIEW_PENDING_APPLICATIONS, PermissionName.MANAGE_OPENINGS, PermissionName.VIEW_REPORTS, PermissionName.REGISTER_USER, PermissionName.RECRUITER_OWN_OPENINGS, PermissionName.RECRUITER_OTHER_OPENINGS, PermissionName.MANAGE_INTERVIEWER_STATUS);
            case EMPLOYEE:
                return List.of(PermissionName.VIEW_ALL_OPENINGS, PermissionName.REGISTER_USER, PermissionName.VIEW_APPLIED_OPENINGS, PermissionName.INTERVIEWER);
        }
        return null;
    }

    /**
     * Retrieves the role of the authenticated user.
     *
     * @return Role of the authenticated user.
     */
    @GetMapping("/authorization/role")
    @Operation(summary = "Get user role", description = "Retrieve the role of the authenticated user")
    @ApiResponse(responseCode = "200", description = "User role retrieved successfully", content = @Content(schema = @Schema(implementation = Role.class)))
    public Role getRole() {
        DBUser user = (DBUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getRole();
    }

    /**
     * Retrieves a public representation of the authenticated user's information.
     *
     * @return PublicUser model representing the authenticated user's information.
     */
    @GetMapping("/authorization/user")
    @Operation(summary = "Get authenticated user", description = "Retrieve a public representation of the authenticated user's information")
    @ApiResponse(responseCode = "200", description = "Authenticated user information retrieved successfully", content = @Content(schema = @Schema(implementation = PublicUser.class)))
    public PublicUser getUser() {
        DBUser user = (DBUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Use the user mapper to convert the DBUser entity to a PublicUser model.
        return userMapper.entityToPublicModel(user);
    }
}
