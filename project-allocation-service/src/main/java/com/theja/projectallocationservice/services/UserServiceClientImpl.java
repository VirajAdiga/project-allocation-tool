package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.dto.PublicUserListResponse;
import com.theja.projectallocationservice.entities.enums.PermissionName;
import com.theja.projectallocationservice.dto.PublicUser;
import com.theja.projectallocationservice.exceptions.ResourceNotFoundException;
import com.theja.projectallocationservice.exceptions.ServiceClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class UserServiceClientImpl implements UserServiceClient {

    @Autowired
    private Environment environment;

    private String getUserServiceHost(){
        return environment.getProperty("USER_SERVICE");
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

        try {
            ResponseEntity<Object> permissions = new RestTemplate().exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Object.class
            );

            List<PermissionName> permissionNames = (List<PermissionName>) permissions.getBody();
            return permissionNames;
        }
        catch (HttpClientErrorException ex) {
            // Handle specific HTTP client errors (4xx)
            throw new ServiceClientException("Error communicating with the service:  " + ex.getStatusText());
        }catch (Exception ex) {
            // Handle other exceptions
            throw new ServiceClientException("An error occurred: " + ex.getMessage());
        }
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

        try {
            ResponseEntity<PublicUser> user = new RestTemplate().exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    PublicUser.class
            );
            return user.getBody();
        }
        catch (HttpClientErrorException ex) {
            // Handle specific HTTP client errors (4xx)
            throw new ServiceClientException("Error communicating with the service:  " + ex.getStatusText());
        }catch (Exception ex) {
            // Handle other exceptions
            throw new ServiceClientException("An error occurred: " + ex.getMessage());
        }
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
        try {
            ResponseEntity<PublicUser> user = new RestTemplate().exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    PublicUser.class
            );
            return user.getBody();
        }
        catch (ResourceNotFoundException ex) {
            throw new ServiceClientException("Resource not found with id: " + userId);
        }
        catch (HttpClientErrorException ex) {
            // Handle specific HTTP client errors (4xx)
            throw new ServiceClientException("Error communicating with the service:  " + ex.getStatusText());
        } catch (Exception ex) {
            // Handle other exceptions
            throw new ServiceClientException("An error occurred: " + ex.getMessage());
        }
    }

    @Override
    public PublicUserListResponse getUsersById(List<Long> userIds) {
        String url = String.format("%sapi/v1/users/public", getUserServiceHost());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>("{\"userIds\": \"" + userIds + "\"}", headers);
        try {
            ResponseEntity<PublicUserListResponse> user = new RestTemplate().exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    PublicUserListResponse.class
            );

            return user.getBody();
        }
        catch (HttpClientErrorException ex) {
            // Handle specific HTTP client errors (4xx)
            throw new ServiceClientException("Error communicating with the service:  " + ex.getStatusText());
        } catch (Exception ex) {
            // Handle other exceptions
            throw new ServiceClientException("An error occurred: " + ex.getMessage());
        }
    }

    @Override
    public void updateUserProjectAllocation(Long userId, Long projectId) {
        String url = String.format("%sapi/v1/users/public/%s", getUserServiceHost(), userId.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>("{\"projectAllocatedId\": \"" + projectId + "\"}", headers);
        try {
            new RestTemplate().exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );
        }
        catch (HttpClientErrorException ex) {
            // Handle specific HTTP client errors (4xx)
            throw new ServiceClientException("Error communicating with the service:  " + ex.getStatusText());
        } catch (Exception ex) {
            // Handle other exceptions
            throw new ServiceClientException("An error occurred: " + ex.getMessage());
        }
    }
}
