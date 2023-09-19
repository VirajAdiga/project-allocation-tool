package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.entities.enums.PermissionName;
import com.theja.projectallocationservice.dto.PublicUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class UserServiceClientImpl implements UserServiceClient {
    /**
     * Get the list of permissions associated with a user.
     *
     * @param authHeader The authentication header containing user credentials.
     * @return A list of permission names.
     */
    @Override
    public List<PermissionName> getPermissions(String authHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Object> permissions = new RestTemplate().exchange(
                "http://localhost:9092/api/v1/authorization/permissions",
                HttpMethod.GET,
                entity,
                Object.class
        );

        List<PermissionName> permissionNames = (List<PermissionName>) permissions.getBody();
        return permissionNames;
    }

    /**
     * Get public user information based on the authentication header.
     *
     * @param authHeader The authentication header containing user credentials.
     * @return Public user information.
     */
    @Override
    public PublicUser getUser(String authHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<PublicUser> user = new RestTemplate().exchange(
                "http://localhost:9092/api/v1/authorization/user",
                HttpMethod.GET,
                entity,
                PublicUser.class
        );

        return user.getBody();
    }
}
