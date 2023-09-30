package com.project.userservice.dto;

import lombok.*;

import java.util.List;

/**
 * Represents a response containing a list of public users and the total number of elements.
 */
@Getter
@AllArgsConstructor
@Builder
public class PublicUserListResponse {
    List<PublicUser> users;  // List of public users in the response
    Long totalElements;      // Total number of elements in the list
}
