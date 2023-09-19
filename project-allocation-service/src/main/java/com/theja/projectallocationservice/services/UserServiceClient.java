package com.theja.projectallocationservice.services;

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
}
