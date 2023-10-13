package com.theja.projectallocationservice.controllers;

import com.theja.projectallocationservice.dto.RequestContext;
import com.theja.projectallocationservice.dto.UpdateFeedbackRequest;
import com.theja.projectallocationservice.entities.enums.InterviewStatus;
import com.theja.projectallocationservice.entities.enums.PermissionName;
import com.theja.projectallocationservice.exceptions.ResourceNotFoundException;
import com.theja.projectallocationservice.exceptions.UnauthorizedAccessException;
import com.theja.projectallocationservice.mappers.InterviewMapper;
import com.theja.projectallocationservice.entities.*;
import com.theja.projectallocationservice.services.ApplicationService;
import com.theja.projectallocationservice.services.AuditCommentService;
import com.theja.projectallocationservice.services.AuditLogService;
import com.theja.projectallocationservice.services.InterviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The `InterviewController` class handles HTTP requests related to interviews.
 * It provides endpoints for retrieving, creating, updating, and managing interview details.
 */
@RestController
@RequestMapping("/api/v1/interviews")
@Tag(name = "Interview Controller", description = "Endpoints related to interview management")
public class InterviewController {
    // Autowired fields for services, mappers, and context...
    @Autowired
    private InterviewService interviewService;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private InterviewMapper interviewMapper;
    @Autowired
    private RequestContext requestContext;
    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    private AuditCommentService auditCommentService;

    /**
     * Retrieves all interviews associated with a particular interviewer ID.
     *
     * @param interviewerId The ID of the interviewer.
     * @return List of interviews associated with the provided interviewer ID.
     */
    @GetMapping("/interviewer/{interviewerId}")
    @Operation(summary = "Get interviews by interviewer ID", description = "Retrieve all interviews with a particular interviewer ID")
    @ApiResponse(responseCode = "200", description = "Interviews retrieved successfully", content = @Content(schema = @Schema(implementation = com.theja.projectallocationservice.dto.Interview.class)))
    public ResponseEntity<List<com.theja.projectallocationservice.dto.Interview>> getInterviewsByInterviewerId(@PathVariable Long interviewerId) {
        // Fetch all interviews associated with the provided interviewer ID
        List<Interview> dbInterviews = interviewService.getInterviewsByInterviewerId(interviewerId);

        // Convert the DBInterview entities to Interview models
        List<com.theja.projectallocationservice.dto.Interview> interviews = dbInterviews.stream()
                .map(interviewMapper::entityToModel)
                .collect(Collectors.toList());

        // Return the list of interviews in the response
        return ResponseEntity.ok(interviews);
    }

    /**
     * Retrieves a list of interviews associated with a specific application ID.
     *
     * @param applicationId The unique identifier of the application
     * @return A ResponseEntity containing the list of interviews and an HTTP status code
     */
    @GetMapping("")
    public ResponseEntity<List<com.theja.projectallocationservice.dto.Interview>> getInterviewsByApplicationId(@RequestParam Long applicationId) {
        // Fetch all interviews associated with the provided application ID
        List<Interview> dbInterviews = interviewService.getInterviewsByApplicationId(applicationId);

        // Convert the DBInterview entities to Interview models
        List<com.theja.projectallocationservice.dto.Interview> interviews = dbInterviews.stream()
                .map(interviewMapper::entityToModel)
                .collect(Collectors.toList());

        // Return the list of interviews in the response
        return ResponseEntity.ok(interviews);
    }

    /**
     * Retrieves an interview by its ID.
     *
     * @param interviewId The ID of the interview to retrieve.
     * @return The interview model corresponding to the provided interview ID.
     */
    @GetMapping("/{interviewId}")
    @Operation(summary = "Get interview by ID", description = "Retrieve an interview by its ID")
    @ApiResponse(responseCode = "200", description = "Interview retrieved successfully", content = @Content(schema = @Schema(implementation = com.theja.projectallocationservice.dto.Interview.class)))
    public ResponseEntity<com.theja.projectallocationservice.dto.Interview> getInterview(@PathVariable Long interviewId) {
        // Fetch an interview by its ID and convert it to a model
        Interview interview = interviewService.getInterviewById(interviewId);
        return ResponseEntity.ok(interviewMapper.entityToModel(interview));
    }

    /**
     * Creates a new interview for a given application.
     *
     * @param applicationId The ID of the application associated with the interview.
     * @param interview   The interview details to be created.
     * @return The created interview model in the response.
     */
    @PostMapping("/applications/{applicationId}")
    @Operation(summary = "Create interview", description = "Create a new interview for a given application")
    @ApiResponse(responseCode = "201", description = "Interview created successfully", content = @Content(schema = @Schema(implementation = com.theja.projectallocationservice.dto.Interview.class)))
    public ResponseEntity<com.theja.projectallocationservice.dto.Interview> createInterview(@PathVariable Long applicationId, @RequestBody Interview interview) {
        // Create an audit log for scheduling an interview
        AuditLog auditLog = auditLogService.createAuditLog(
                AuditLog.builder()
                        .action("Scheduling interview for application id " + applicationId)
                        .userId(requestContext.getLoggedinUser().getId())
                        .loggedAt(new Date())
                        .auditComments(new ArrayList<>())
                        .build());
        // Check user permissions to schedule an interview
        auditCommentService.createAuditComment(AuditComment.builder()
                .comment("Checking user permissions")
                .auditLog(auditLog)
                .build());
        if (requestContext.getPermissions() == null || !requestContext.getPermissions().contains(PermissionName.VIEW_PENDING_APPLICATIONS.toString())) {
            throw new UnauthorizedAccessException("You don't have permission to schedule an interview.");
        }
        // Fetch the associated application by ID
        Application application = applicationService.getApplicationById(applicationId);
        auditCommentService.createAuditComment(AuditComment.builder()
                .comment("Application with id " + applicationId + " found")
                .auditLog(auditLog)
                .build());
        // Schedule the interview and update its status
        interview.setApplication(application);
        interview.setStatus(InterviewStatus.SCHEDULED);
        interview.setFeedback("");
        // Save the created interview and log the action
        Interview dbCreatedInterview = interviewService.createInterview(interview);
        auditCommentService.createAuditComment(AuditComment.builder()
                .comment("Interview created successfully")
                .auditLog(auditLog)
                .build());
        // Return the response with a CREATED status
        return ResponseEntity.status(HttpStatus.CREATED).body(interviewMapper.entityToModel(dbCreatedInterview));
    }

    /**
     * Updates an existing interview's details.
     *
     * @param interviewId The ID of the interview to update.
     * @param interview The updated interview details.
     * @return The updated interview model in the response.
     */
    @PutMapping("/{interviewId}")
    @Operation(summary = "Update interview", description = "Update an existing interview's details")
    @ApiResponse(responseCode = "200", description = "Interview updated successfully", content = @Content(schema = @Schema(implementation = com.theja.projectallocationservice.dto.Interview.class)))
    public ResponseEntity<com.theja.projectallocationservice.dto.Interview> updateInterview(@PathVariable Long interviewId, @RequestBody Interview interview) {
        // Check user permissions to update interview details
        if (requestContext.getPermissions() == null || !requestContext.getPermissions().contains(PermissionName.VIEW_PENDING_APPLICATIONS.toString())) {
            throw new UnauthorizedAccessException("You don't have permission to update the interview details.");
        }
        // Update the interview and save the changes
        Interview dbUpdatedInterview = interviewService.updateInterview(interviewId, interview);
        // Return the updated interview
        return ResponseEntity.ok(interviewMapper.entityToModel(dbUpdatedInterview));
    }

    @PatchMapping("/{interviewId}/feedback")
    public ResponseEntity<String> updateInterviewFeedback(@PathVariable Long interviewId, @RequestBody UpdateFeedbackRequest request) {
        // Fetch the interview by ID
        Interview interview = interviewService.getInterviewById(interviewId);
        // Update the feedback
        interview.setFeedback(request.getFeedback());

        // Save the updated interview
        interviewService.updateInterview(interviewId, interview);

        // Return a success response
        return ResponseEntity.ok("Feedback updated successfully");
    }

    /**
     * Updates the status of an existing interview.
     *
     * @param interviewId The ID of the interview to update.
     * @param newStatus   The new status to set for the interview.
     * @return The updated interview model in the response.
     */
    @PatchMapping("/{interviewId}/status")
    @Operation(summary = "Update interview status", description = "Update the status of an existing interview")
    @ApiResponse(responseCode = "200", description = "Interview status updated successfully", content = @Content(schema = @Schema(implementation = com.theja.projectallocationservice.dto.Interview.class)))
    public ResponseEntity<com.theja.projectallocationservice.dto.Interview> updateInterviewStatus(@PathVariable Long interviewId, @RequestParam InterviewStatus newStatus) {
        // Create an audit log for updating interview status
        AuditLog auditLog = auditLogService.createAuditLog(
                AuditLog.builder()
                        .action("Updating status of interview with id " + interviewId)
                        .userId(requestContext.getLoggedinUser().getId())
                        .loggedAt(new Date())
                        .auditComments(new ArrayList<>())
                        .build());
        // Check user permissions to update interview status
        auditCommentService.createAuditComment(AuditComment.builder()
                .comment("Checking user permissions")
                .auditLog(auditLog)
                .build());
        if (requestContext.getPermissions() == null || !requestContext.getPermissions().contains(PermissionName.VIEW_PENDING_APPLICATIONS.toString())) {
            throw new UnauthorizedAccessException("You don't have permission to update the interview status.");
        }
        // Fetch the interview by ID
        Interview interview = interviewService.getInterviewById(interviewId);
        auditCommentService.createAuditComment(AuditComment.builder()
                .comment("Interview with id " + interviewId + " found")
                .auditLog(auditLog)
                .build());
        if (newStatus == InterviewStatus.SCHEDULED) {
            return ResponseEntity.badRequest().build();
        } else if (newStatus == InterviewStatus.COMPLETED && interview.getStatus() != InterviewStatus.SCHEDULED) {
            return ResponseEntity.badRequest().build();
        } else if (newStatus == InterviewStatus.CANCELLED && interview.getStatus() != InterviewStatus.SCHEDULED) {
            return ResponseEntity.badRequest().build();
        }
        // Update the interview status based on newStatus
        interview.setStatus(newStatus);
        // Save the updated interview and log the action
        Interview dbUpdatedInterview = interviewService.updateInterview(interviewId, interview);
        auditCommentService.createAuditComment(AuditComment.builder()
                .comment("Interview status updated successfully")
                .auditLog(auditLog)
                .build());
        // Return the response with the updated interview
        return ResponseEntity.ok(interviewMapper.entityToModel(dbUpdatedInterview));
    }
}
