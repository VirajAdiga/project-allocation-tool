package com.theja.projectallocationservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class AllocatedPoolUserResponse {
        List<PublicUser> allocatedUsers; // List of allocated users' public information
        Long totalElements; // Total number of elements in the response
}
