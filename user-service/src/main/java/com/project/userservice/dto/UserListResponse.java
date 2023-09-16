package com.project.userservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
// Model class representing a response containing a list of users.
public class UserListResponse {
    List<PublicUser> users;   // List of public user profiles.
    Long totalElements;       // Total count of elements in the response.
}
