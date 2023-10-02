package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.dto.PublicUserListResponse;
import com.theja.projectallocationservice.entities.enums.PermissionName;
import com.theja.projectallocationservice.dto.PublicUser;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class UserServiceClientImpl implements UserServiceClient {

    @Autowired
    private Dotenv dotenv;

    private String getUserServiceHost(){
        return dotenv.get("USER_SERVICE");
    }

    /**
     * Get the list of permissions associated with a user.
     *
     * @param authHeader The authentication header containing user credentials.
     * @return A list of permission names.
     */
    @Override
    public List<PermissionName> getPermissions(String authHeader) {
        String url = String.format("%sapi/v1/authorization/permissions", getUserServiceHost());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Object> permissions = new RestTemplate().exchange(
                url,
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
        String url = String.format("%sapi/v1/authorization/user", getUserServiceHost());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<PublicUser> user = new RestTemplate().exchange(
                url,
                HttpMethod.GET,
                entity,
                PublicUser.class
        );
        return user.getBody();
    }

    /**
     * Get public user information based on the user id.
     *
     * @param userId The user id of the user
     * @return Public user information.
     */
    @Override
    public PublicUser getUserById(Long userId) {
        String url = String.format("%sapi/v1/users/public/%s", getUserServiceHost(), userId.toString());
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<PublicUser> user = new RestTemplate().exchange(
                url,
                HttpMethod.GET,
                entity,
                PublicUser.class
        );
        return user.getBody();
    }

    @Override
    public PublicUserListResponse getUsersById(List<Long> userIds) {
        String url = String.format("%sapi/v1/users/public", getUserServiceHost());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>("{\"userIds\": \"" + userIds + "\"}", headers);

        ResponseEntity<PublicUserListResponse> user = new RestTemplate().exchange(
                url,
                HttpMethod.POST,
                entity,
                PublicUserListResponse.class
        );

        return user.getBody();
    }

    @Override
    public void updateUserProjectAllocation(Long userId, Long projectId) {
        String url = String.format("%sapi/v1/users/public/%s", getUserServiceHost(), userId.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>("{\"projectAllocatedId\": \"" + projectId + "\"}", headers);

        new RestTemplate().exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );
    }
}
