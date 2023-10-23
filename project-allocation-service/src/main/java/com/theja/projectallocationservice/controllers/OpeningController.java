package com.theja.projectallocationservice.controllers;

import com.theja.projectallocationservice.dto.OpeningsListResponse;
import com.theja.projectallocationservice.dto.RequestContext;
import com.theja.projectallocationservice.entities.enums.OpeningStatus;
import com.theja.projectallocationservice.entities.enums.PermissionName;
import com.theja.projectallocationservice.exceptions.*;
import com.theja.projectallocationservice.mappers.OpeningMapper;
import com.theja.projectallocationservice.entities.*;
import com.theja.projectallocationservice.services.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The `OpeningController` class handles HTTP requests related to openings and their management.
 * It provides endpoints for retrieving, creating, updating, and deleting openings.
 */
@RestController
@RequestMapping("/api/v1/openings")
@Tag(name = "Openings", description = "Endpoints related to opening management")
public class OpeningController {
    // Autowired fields for services, mappers, and context...
    @Autowired
    private OpeningService openingService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private OpeningMapper openingMapper;
    @Autowired
    private SkillService skillService;
    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    private RequestContext requestContext;
    @Autowired
    private AuditCommentService auditCommentService;

    /**
     * Retrieves a list of openings with optional pagination and filters.
     *
     * @param pageSize     The number of items per page.
     * @param pageNumber   The page number.
     * @param appliedBySelf Filter by openings applied by the logged-in user.
     * @param postedBySelf Filter by openings posted by the logged-in user.
     * @return A response containing a list of openings and pagination details.
     */
    // Get all openings
    @GetMapping("")
    @Operation(summary = "Get openings", description = "Retrieve a list of openings with optional pagination and filters")
    @ApiResponse(responseCode = "200", description = "Openings retrieved successfully", content = @Content(schema = @Schema(implementation = OpeningsListResponse.class)))
    public ResponseEntity<OpeningsListResponse> getAllOpenings(@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Boolean appliedBySelf, @RequestParam(required = false) Boolean postedBySelf) {
        // Fetch all openings and return a list of opening models with pagination details
        Page<Opening> dbOpenings = openingService.getAllOpenings(pageSize, pageNumber, appliedBySelf, postedBySelf);
        OpeningsListResponse response = OpeningsListResponse.builder()
                .openings(openingMapper.entityToModel(dbOpenings.getContent()))
                .totalElements(dbOpenings.getTotalElements())
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a list of openings for a specific project.
     *
     * @param projectId The ID of the project.
     * @return A response containing a list of openings for the specified project.
     */
    // Get openings for a specific project
    @GetMapping("/projects/{projectId}")
    @Operation(summary = "Get openings for a project", description = "Retrieve a list of openings for a specific project")
    @ApiResponse(responseCode = "200", description = "Openings retrieved successfully", content = @Content(schema = @Schema(implementation = List.class)))
    public ResponseEntity<List<com.theja.projectallocationservice.dto.Opening>> getAllOpeningsForProject(@PathVariable Long projectId) {
        // Fetch openings for a specific project and return a list of opening models
        List<Opening> openings = openingService.getAllOpeningsForProject(projectId);
        return ResponseEntity.ok(openingMapper.entityToModel(openings));
    }

    /**
     * Retrieves a specific opening by its ID.
     *
     * @param id The ID of the opening.
     * @return A response containing the details of the specified opening.
     * @throws OpeningNotFoundException If the opening with the given ID is not found.
     */
    // Get a specific opening by ID
    @GetMapping("/{id}")
    @Operation(summary = "Get opening by ID", description = "Retrieve a specific opening by its ID")
    @ApiResponse(responseCode = "200", description = "Opening retrieved successfully", content = @Content(schema = @Schema(implementation = com.theja.projectallocationservice.dto.Opening.class)))
    public ResponseEntity<com.theja.projectallocationservice.dto.Opening> getOpeningById(@PathVariable("id") Long id) {
        // Fetch an opening by its ID and return it as a model
        Opening opening = openingService.getOpeningById(id);
        return ResponseEntity.ok(openingMapper.entityToModel(opening));
    }

    /**
     * Creates a new opening for a specific project.
     *
     * @param opening  The opening details.
     * @param projectId  The ID of the project.
     * @return A response containing the details of the created opening.
     * @throws UnauthorizedAccessException If the user does not have permission to create an opening.
     * @throws SkillNotFoundException      If a skill associated with the opening is not found.
     */
    @PostMapping("/projects/{projectId}")
    @Operation(summary = "Create opening", description = "Create a new opening for a specific project")
    @ApiResponse(responseCode = "201", description = "Opening created successfully", content = @Content(schema = @Schema(implementation = com.theja.projectallocationservice.dto.Opening.class)))
    public ResponseEntity<com.theja.projectallocationservice.dto.Opening> createOpening(@RequestBody @Validated Opening opening, @PathVariable Long projectId) {
        // Create an audit log for creating an opening
        AuditLog auditLog = auditLogService.createAuditLog(
                AuditLog.builder()
                        .action("Create Opening for project " + projectId)
                        .userId(requestContext.getLoggedinUser().getId())
                        .loggedAt(new Date())
                        .auditComments(new ArrayList<>())
                        .build());
        auditCommentService.createAuditComment(AuditComment.builder()
                        .comment("Checking user permissions")
                        .auditLog(auditLog)
                .build());
        // Check user permissions to create an opening
        if (requestContext.getPermissions() == null || !requestContext.getPermissions().contains(PermissionName.CREATE_OPENING.toString())) {
            auditCommentService.createAuditComment(AuditComment.builder()
                    .comment("Unauthorized user trying to create opening")
                    .auditLog(auditLog)
                    .build());
            throw new UnauthorizedAccessException("You don't have permission to create an opening.");
        }
        auditCommentService.createAuditComment(AuditComment.builder()
                .comment("Permissions passed")
                .auditLog(auditLog)
                .build());
        auditCommentService.createAuditComment(AuditComment.builder()
                .comment("Checking if opening is duplicate")
                .auditLog(auditLog)
                .build());
        // Check if a duplicate opening with the same attributes already exists for the given project
        openingService.isDuplicateOpening(opening, projectId);
        auditCommentService.createAuditComment(AuditComment.builder()
                .comment("Opening is unique")
                .auditLog(auditLog)
                .build());
        // Map the opening to a project and validate skills
        opening.setProject(projectService.getProjectById(projectId));
        opening.setRecruiterId(requestContext.getLoggedinUser().getId());
        auditCommentService.createAuditComment(AuditComment.builder()
                .comment("Project and recruiter mapped to opening")
                .auditLog(auditLog)
                .build());
        // Retrieve the Skill entities from the database using the provided skillIds
        List<Skill> skills = new ArrayList<>();
        for (Skill skill : opening.getSkills()) {
            skills.add(skillService.getSkillById(skill.getId()));
        }
        opening.setSkills(skills);
        auditCommentService.createAuditComment(AuditComment.builder()
                .comment("Skills assigned to opening")
                .auditLog(auditLog)
                .build());
        // Set the opening status, create it, and return the response
        opening.setStatus(OpeningStatus.ACTIVE);
        Opening dbCreatedOpening = openingService.createOpening(opening, requestContext.getLoggedinUser());
        com.theja.projectallocationservice.dto.Opening createdOpening = openingMapper.entityToModel(dbCreatedOpening);
        auditCommentService.createAuditComment(AuditComment.builder()
                .comment("Opening created")
                .auditLog(auditLog)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOpening);
    }

    /**
     * Updates the details of an existing opening.
     *
     * @param id        The ID of the opening to update.
     * @param opening The updated opening details.
     * @return A response containing the updated opening details.
     * @throws UnauthorizedAccessException If the user does not have permission to update an opening.
     * @throws OpeningNotFoundException    If the opening with the given ID is not found.
     * @throws OpeningUpdateException      If the update of the opening fails.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update opening", description = "Update the details of an existing opening")
    @ApiResponse(responseCode = "200", description = "Opening updated successfully", content = @Content(schema = @Schema(implementation = com.theja.projectallocationservice.dto.Opening.class)))
    public ResponseEntity<com.theja.projectallocationservice.dto.Opening> updateOpening(
            @PathVariable("id") Long id,
            @RequestBody Opening opening) {
        // Create an audit log for updating an opening
        AuditLog auditLog = auditLogService.createAuditLog(
                AuditLog.builder()
                        .action("Update Opening " + id)
                        .userId(requestContext.getLoggedinUser().getId())
                        .loggedAt(new Date())
                        .auditComments(new ArrayList<>())
                        .build());
        auditCommentService.createAuditComment(AuditComment.builder()
                .comment("Checking user permissions")
                .auditLog(auditLog)
                .build());
        // Check user permissions to update an opening
        if (requestContext.getPermissions() == null || !requestContext.getPermissions().contains(PermissionName.MANAGE_OPENINGS.toString())) {
            auditCommentService.createAuditComment(AuditComment.builder()
                    .comment("Unauthorized user trying to create opening")
                    .auditLog(auditLog)
                    .build());
            throw new UnauthorizedAccessException("You don't have permission to create an opening.");
        }
        // Fetch the existing opening and update its properties
        Opening existingOpening = openingService.getOpeningById(id);
        auditCommentService.createAuditComment(AuditComment.builder()
                .comment("Opening with " + id + " found")
                .auditLog(auditLog)
                .build());
        // Update the properties of the existingOpening with the new values from the request payload
        existingOpening.setTitle(opening.getTitle());
        existingOpening.setDetails(opening.getDetails());
        existingOpening.setLevel(opening.getLevel());
        existingOpening.setLocation(opening.getLocation());
        existingOpening.setStatus(opening.getStatus());
        existingOpening.setSkills(opening.getSkills());
        auditCommentService.createAuditComment(AuditComment.builder()
                .comment("Updated opening with new values")
                .auditLog(auditLog)
                .build());
        // Save the updated opening
        Opening updatedOpening = openingService.updateOpening(id, existingOpening);
        auditCommentService.createAuditComment(AuditComment.builder()
                .comment("Opening updated")
                .auditLog(auditLog)
                .build());
        // Return the updated opening in the response
        com.theja.projectallocationservice.dto.Opening responseOpening = openingMapper.entityToModel(updatedOpening);
        return ResponseEntity.ok(responseOpening);
    }

    // Update opening status
    @PatchMapping("/{id}/status")
    @Operation(summary = "Update opening status", description = "Update the status of an existing opening")
    @ApiResponse(responseCode = "200", description = "Opening status updated successfully", content = @Content(schema = @Schema(implementation = com.theja.projectallocationservice.dto.Opening.class)))
    public ResponseEntity<com.theja.projectallocationservice.dto.Opening> updateOpeningStatus(@PathVariable Long id, @RequestParam OpeningStatus newStatus) {
        // Fetch the opening by ID and update its status
        Opening opening = openingService.getOpeningById(id);
        OpeningStatus currentStatus = opening.getStatus();

        if (currentStatus == OpeningStatus.ACTIVE) {
            if (newStatus == OpeningStatus.PENDING || newStatus == OpeningStatus.CLOSED) {
                opening.setStatus(newStatus);
                Opening dbUpdatedOpening = openingService.updateOpening(id, opening);
                return ResponseEntity.ok(openingMapper.entityToModel(dbUpdatedOpening));
            }
        } else if (currentStatus == OpeningStatus.PENDING) {
            if (newStatus == OpeningStatus.ACTIVE || newStatus == OpeningStatus.CLOSED) {
                opening.setStatus(newStatus);
                Opening dbUpdatedOpening = openingService.updateOpening(id, opening);
                return ResponseEntity.ok(openingMapper.entityToModel(dbUpdatedOpening));
            }
        } else if (currentStatus == OpeningStatus.CLOSED) {
            if (newStatus == OpeningStatus.CLOSED) {
                opening.setStatus(newStatus);
                Opening dbUpdatedOpening = openingService.updateOpening(id, opening);
                return ResponseEntity.ok(openingMapper.entityToModel(dbUpdatedOpening));
            }
        }

        // If the requested status transition is not allowed, return bad request
        return ResponseEntity.badRequest().build();
    }

    // Delete an opening
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete opening", description = "Delete an existing opening by its ID")
    @ApiResponse(responseCode = "204", description = "Opening deleted successfully")
    public ResponseEntity<Void> deleteOpening(@PathVariable("id") Long id) {
        // Delete the opening by ID and return the appropriate response
        openingService.deleteOpening(id);
        return ResponseEntity.noContent().build();
    }
}
