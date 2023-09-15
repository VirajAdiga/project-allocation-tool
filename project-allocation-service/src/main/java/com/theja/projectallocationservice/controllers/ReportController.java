package com.theja.projectallocationservice.controllers;

import com.theja.projectallocationservice.exceptions.UnauthorizedAccessException;
import com.theja.projectallocationservice.mappers.UserMapper;
import com.theja.projectallocationservice.models.*;
import com.theja.projectallocationservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * The `ReportController` class manages HTTP requests related to generating reports.
 * It provides endpoints for retrieving various types of reports based on user permissions.
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Reports", description = "Endpoints related to generating reports")
public class ReportController {
    // Autowired fields for services, mappers, and context...
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RequestContext requestContext;

    /**
     * Retrieves a paginated list of free pool users' report.
     *
     * @param pageSize   The number of users per page.
     * @param pageNumber The page number of users to retrieve.
     * @return The response containing a list of free pool user models with pagination details.
     */
    // Get all free pool users report
    @GetMapping("/reports/users/free")
    @Operation(summary = "Get all free pool users report", description = "Retrieve a paginated list of free pool users' report")
    @ApiResponse(responseCode = "200", description = "Free pool users report retrieved successfully", content = @Content(schema = @Schema(implementation = FreePoolUserResponse.class)))
    public ResponseEntity<FreePoolUserResponse> getAllFreePoolUsers(@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageNumber) {
        // Check user permissions to view reports
        if (requestContext.getPermissions() == null || !requestContext.getPermissions().contains(PermissionName.VIEW_REPORTS.toString())) {
            throw new UnauthorizedAccessException("You don't have permission to access any reports.");
        }
        // Fetch free pool users and return the response with pagination details
        Page<DBUser> dbUsers = userService.getFreePoolUsers(pageSize, pageNumber);
        FreePoolUserResponse response = FreePoolUserResponse.builder()
                .freePoolUsers(userMapper.entityToPublicModel(dbUsers.getContent()))
                .totalElements(dbUsers.getTotalElements())
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a paginated list of allocated users' report within a specified date range.
     *
     * @param startDate  The start date of the allocation period.
     * @param endDate    The end date of the allocation period.
     * @param pageSize   The number of users per page.
     * @param pageNumber The page number of users to retrieve.
     * @return The response containing a list of allocated user models with pagination details.
     * @throws ParseException If there's an error parsing the provided date strings.
     */
    // Get all allocated users report within a date range
    @GetMapping("/reports/users/allocated")
    @Operation(summary = "Get all allocated users report", description = "Retrieve a paginated list of allocated users' report within a specified date range")
    @ApiResponse(responseCode = "200", description = "Allocated users report retrieved successfully", content = @Content(schema = @Schema(implementation = AllocatedPoolUserResponse.class)))
    public ResponseEntity<AllocatedPoolUserResponse> getAllAllocatedUsers(@RequestParam String startDate, @RequestParam String endDate, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageNumber) throws ParseException {
        // Check user permissions to view reports
        if (requestContext.getPermissions() == null || !requestContext.getPermissions().contains(PermissionName.VIEW_REPORTS.toString())) {
            throw new UnauthorizedAccessException("You don't have permission to access any reports.");
        }
        // Parse the provided date range
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        // Fetch allocated users within the date range and return the response with pagination details
        Page<DBUser> dbUsers = userService.getAllAllocatedUsers(formatter.parse(startDate), formatter.parse(endDate), pageSize, pageNumber);
        AllocatedPoolUserResponse response = AllocatedPoolUserResponse.builder()
                .allocatedUsers(userMapper.entityToPublicModel(dbUsers.getContent()))
                .totalElements(dbUsers.getTotalElements())
                .build();
        return ResponseEntity.ok(response);
    }
}
