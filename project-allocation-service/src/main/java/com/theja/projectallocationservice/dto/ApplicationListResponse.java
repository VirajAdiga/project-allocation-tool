package com.theja.projectallocationservice.dto;

import com.theja.projectallocationservice.dto.Application;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

// Using Lombok annotations to simplify class creation and getter methods
@Builder
@Getter
public class ApplicationListResponse {

    // List to hold Application objects
    List<Application> applications;

    // Total number of elements in the response
    Long totalElements;
}
