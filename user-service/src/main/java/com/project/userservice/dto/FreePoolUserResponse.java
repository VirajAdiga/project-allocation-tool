package com.project.userservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Represents a response containing a list of users available in the free pool along with the total number of users.
 */
@Builder
@Getter
public class FreePoolUserResponse {
    List<PublicUser> freePoolUsers;  // List of users available in the free pool
    Long totalElements;  // Total number of users in the free pool
}
