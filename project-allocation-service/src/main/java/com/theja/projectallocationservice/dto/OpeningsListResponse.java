package com.theja.projectallocationservice.dto;

import com.theja.projectallocationservice.dto.Opening;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Represents a response containing a list of openings along with the total number of elements.
 */
@Builder
@Getter
public class OpeningsListResponse {
    List<Opening> openings;  // List of openings
    Long totalElements;      // Total number of elements (openings) in the response
}

