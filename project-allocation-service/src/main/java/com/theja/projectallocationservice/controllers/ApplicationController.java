package com.theja.projectallocationservice.controllers;

import com.theja.projectallocationservice.exceptions.ApplicationNotFoundException;
import com.theja.projectallocationservice.exceptions.ResourceNotFoundException;
import com.theja.projectallocationservice.exceptions.UnauthorizedAccessException;
import com.theja.projectallocationservice.mappers.ApplicationMapper;
import com.theja.projectallocationservice.models.*;
import com.theja.projectallocationservice.services.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controller class for managing application-related endpoints.
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Application Controller", description = "Endpoints related to application management")
public class ApplicationController {
    // Autowired fields for various services and mappers...
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private OpeningService openingService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ApplicationMapper applicationMapper;
    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    private RequestContext requestContext;
    @Autowired
    private AuditCommentService auditCommentService;

    /**
     * Retrieve a paginated list of applications with optional status filter.
     *
     * @param status    Optional status filter for applications.
     * @param pageNumber    Page number for pagination.
     * @param pageSize    Number of items per page.
     * @return ResponseEntity containing the list of applications and pagination details.
     */
    @GetMapping("/applications")
    @Operation(summary = "Get all applications", description = "Retrieve a paginated list of applications with optional status filter")
    public ResponseEntity<ApplicationListResponse> getAllApplications(
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        if (requestContext.getPermissions() == null || !requestContext.getPermissions().contains(PermissionName.VIEW_PENDING_APPLICATIONS.toString())) {
            // Create Audit log
            DBAuditLog auditLog = auditLogService.createAuditLog(
                    DBAuditLog.builder()
                            .action("Fetching all pending applications")
                            .user(DBUser.builder().id(requestContext.getLoggedinUser().getId()).build())
                            .loggedAt(new Date())
                            .auditComments(new ArrayList<>())
                            .build());
            auditCommentService.createAuditComment(DBAuditComment.builder()
                    .comment("Unauthorized user trying to fetch all pending applications")
                    .auditLog(auditLog)
                    .build());
            throw new UnauthorizedAccessException("You don't have permission to fetch all pending applications.");
        }
        // Fetch applications with filtering and pagination
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<DBApplication> pageResult = applicationService.getAllApplicationsByStatus(status, pageable);

        List<Application> applicationList = applicationMapper.entityToModel(pageResult.getContent());

        ApplicationListResponse response = ApplicationListResponse.builder()
                .applications(applicationList)
                .totalElements(pageResult.getTotalElements())
                .build();

        // Build the response containing the list of applications and total elements
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve an application by its ID.
     *
     * @param id    ID of the application to retrieve.
     * @return ResponseEntity containing the retrieved application.
     */
    @GetMapping("/applications/{id}")
    @Operation(summary = "Get application by ID", description = "Retrieve an application by its ID")
    @ApiResponse(responseCode = "200", description = "Application retrieved successfully", content = @Content(schema = @Schema(implementation = Application.class)))
    @ApiResponse(responseCode = "404", description = "Application not found")
    public ResponseEntity<Application> getApplication(@PathVariable Long id) {
        // Fetch a specific application by ID and return it in the response
        DBApplication dbApplication = applicationService.getApplicationById(id);
        return ResponseEntity.ok(applicationMapper.entityToModel(dbApplication));
    }

    /**
     * Retrieve application details based on openingId and candidateId.
     *
     * @param openingId    ID of the opening associated with the application.
     * @param candidateId    ID of the candidate associated with the application.
     * @return ResponseEntity containing the retrieved application details.
     */
    @GetMapping("/applications/details")
    @Operation(summary = "Get application details", description = "Retrieve application details based on openingId and candidateId")
    @ApiResponse(responseCode = "200", description = "Application details retrieved successfully", content = @Content(schema = @Schema(implementation = Application.class)))
    @ApiResponse(responseCode = "404", description = "Application not found")
    public ResponseEntity<Application> getApplicationDetails(
            @RequestParam Long openingId,
            @RequestParam Long candidateId
    ) {
        // Fetch application details based on openingId and candidateId
        DBApplication dbApplication = applicationService.getApplicationByOpeningIdAndCandidateId(openingId, candidateId);

        // If found, return the application; otherwise, throw ResourceNotFoundException
        if (dbApplication != null) {
            return ResponseEntity.ok(applicationMapper.entityToModel(dbApplication));
        } else {
            throw new ResourceNotFoundException("Application not found with opening ID: " + openingId + " or candidate ID " + candidateId);
        }
    }

    /**
     * Apply for an opening by creating an application.
     *
     * @param openingId    ID of the opening to apply for.
     * @param application    Application details to be created.
     * @return ResponseEntity containing the created application.
     */
    @PostMapping("/openings/{openingId}/applications")
    @Operation(summary = "Create application", description = "Apply for an opening by creating an application")
    @ApiResponse(responseCode = "201", description = "Application created successfully", content = @Content(schema = @Schema(implementation = Application.class)))
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "404", description = "Opening not found")
    public ResponseEntity<Application> createApplication(@PathVariable Long openingId, @RequestBody DBApplication application) {
        // Create Audit log
        DBAuditLog auditLog = auditLogService.createAuditLog(
                DBAuditLog.builder()
                        .action("Applying for opening " + openingId)
                        .user(DBUser.builder().id(requestContext.getLoggedinUser().getId()).build())
                        .loggedAt(new Date())
                        .auditComments(new ArrayList<>())
                        .build());
        // Fetch the corresponding opening from the OpeningService using openingId
        DBOpening opening = openingService.getOpeningById(openingId);
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Opening with id " + openingId + " found")
                .auditLog(auditLog)
                .build());
        // Opening
        if (opening != null) {
            // Associate the application with the opening
            application.setOpening(opening);
            application.setStatus(ApplicationStatus.APPLIED);
            application.setAppliedAt(new Date());
            application.setInterviews(new ArrayList<>());
            // Save the application to the database
            DBApplication dbCreatedApplication = applicationService.createApplication(application);
            auditCommentService.createAuditComment(DBAuditComment.builder()
                    .comment("Applied for opening successfully")
                    .auditLog(auditLog)
                    .build());
            // Create a new application for the specified opening
            // Save the application and return it in the response
            return ResponseEntity.status(HttpStatus.CREATED).body(applicationMapper.entityToModel(dbCreatedApplication));
        } else {
            throw new ResourceNotFoundException("Opening not found with ID: " + openingId);
        }
    }

    /**
     * Update an existing application by its ID.
     *
     * @param id    ID of the application to update.
     * @param application    Updated application details.
     * @return ResponseEntity containing the updated application.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update application", description = "Update an existing application by its ID")
    @ApiResponse(responseCode = "200", description = "Application updated successfully", content = @Content(schema = @Schema(implementation = DBApplication.class)))
    @ApiResponse(responseCode = "404", description = "Application not found")
    public ResponseEntity<DBApplication> updateApplication(@PathVariable Long id, @RequestBody DBApplication application) {
        // Update an existing application by ID and return the updated application
        DBApplication dbUpdatedApplication = applicationService.updateApplication(id, application);
        return ResponseEntity.ok(dbUpdatedApplication);
    }

    /**
     * Delete an application by its ID.
     *
     * @param id    ID of the application to delete.
     * @return ResponseEntity indicating the success of the deletion.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete application", description = "Delete an application by its ID")
    @ApiResponse(responseCode = "204", description = "Application deleted successfully")
    @ApiResponse(responseCode = "404", description = "Application not found")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        // Delete an application by ID and return a no-content response
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Update the status of an application based on newStatus.
     *
     * @param applicationId    ID of the application to update.
     * @param newStatus    New status to set for the application.
     * @return ResponseEntity containing the updated application.
     */
    @PatchMapping("/applications/{applicationId}/status")
    @Operation(summary = "Update application status", description = "Update the status of an application based on newStatus")
    @ApiResponse(responseCode = "200", description = "Application status updated successfully", content = @Content(schema = @Schema(implementation = Application.class)))
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Application not found")
    public ResponseEntity<Application> updateInterviewStatus(@PathVariable Long applicationId, @RequestParam ApplicationStatus newStatus) {
        // Create Audit log
        DBAuditLog auditLog = auditLogService.createAuditLog(
                DBAuditLog.builder()
                        .action("Updating status of application id " + applicationId)
                        .user(DBUser.builder().id(requestContext.getLoggedinUser().getId()).build())
                        .loggedAt(new Date())
                        .auditComments(new ArrayList<>())
                        .build());
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Checking user permissions")
                .auditLog(auditLog)
                .build());
        if (requestContext.getPermissions() == null || !requestContext.getPermissions().contains(PermissionName.VIEW_PENDING_APPLICATIONS.toString())) {
            auditCommentService.createAuditComment(DBAuditComment.builder()
                    .comment("Unauthorized user trying to update application status")
                    .auditLog(auditLog)
                    .build());
            throw new UnauthorizedAccessException("You don't have permission to update the application status.");
        }
        DBApplication application = applicationService.getApplicationById(applicationId);
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Application with " + applicationId + " found")
                .auditLog(auditLog)
                .build());
        if (application != null) {
            if (newStatus == ApplicationStatus.APPLIED) {
                return ResponseEntity.badRequest().build();
            } else if (newStatus == ApplicationStatus.REJECTED && application.getStatus() != ApplicationStatus.APPLIED) {
                return ResponseEntity.badRequest().build();
            } else if (newStatus == ApplicationStatus.ALLOCATED && application.getStatus() != ApplicationStatus.APPLIED) {
                return ResponseEntity.badRequest().build();
            }
            // Update the status of an application based on provided rules
            application.setStatus(newStatus);
            DBApplication dbUpdatedApplication = applicationService.updateApplication(applicationId, application);
            auditCommentService.createAuditComment(DBAuditComment.builder()
                    .comment("Updated the application status")
                    .auditLog(auditLog)
                    .build());
            if (newStatus != ApplicationStatus.REJECTED) {
                projectService.allocateUser(dbUpdatedApplication.getOpening().getProject(), dbUpdatedApplication.getCandidate());
                auditCommentService.createAuditComment(DBAuditComment.builder()
                        .comment("Application accepted and applicant is allocated to the project")
                        .auditLog(auditLog)
                        .build());
            } else {
                auditCommentService.createAuditComment(DBAuditComment.builder()
                        .comment("Application rejected")
                        .auditLog(auditLog)
                        .build());
            }
            // Return the updated application or appropriate error responses
            return ResponseEntity.ok(applicationMapper.entityToModel(dbUpdatedApplication));
        }
        throw new ApplicationNotFoundException(applicationId);
    }
}

