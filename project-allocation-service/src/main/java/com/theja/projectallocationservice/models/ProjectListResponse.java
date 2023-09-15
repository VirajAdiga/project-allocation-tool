package com.theja.projectallocationservice.models;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Represents a response containing a list of projects and the total number of elements.
 */
@Builder
@Getter
public class ProjectListResponse {
    List<Project> projects;  // List of projects in the response
    Long totalElements;      // Total number of elements in the list
}
