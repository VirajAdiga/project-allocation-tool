package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.dto.PublicUserListResponse;
import com.theja.projectallocationservice.entities.enums.PermissionName;
import com.theja.projectallocationservice.dto.PublicUser;

import java.util.List;

public interface UserServiceClient {
    /**
     * Get the list of permissions associated with a user.
     *
     * @param userToken The authentication token of the user.
     * @return A list of permission names.
     */
    List<PermissionName> getPermissions(String userToken);

    /**
     * Get public user information based on the authentication header.
     *
     * @param authHeader The authentication header containing user credentials.
     * @return Public user information.
     */
    PublicUser getUser(String authHeader);

    /**
     * Get public user information based on the user id.
     *
     * @param userId The user id of the user.
     * @return Public user information.
     */
    PublicUser getUserById(Long userId);

    /**
     * Get list of public user information based on the user ids.
     *
     * @param userIds The user ids of the users.
     * @return List of Public user information.
     */
    PublicUserListResponse getUsersById(List<Long> userIds);

    void updateUserProjectAllocation(Long userId, Long projectId);
}
