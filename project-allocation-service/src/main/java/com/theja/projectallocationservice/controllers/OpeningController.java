package com.theja.projectallocationservice.controllers;

import com.theja.projectallocationservice.exceptions.*;
import com.theja.projectallocationservice.mappers.OpeningMapper;
import com.theja.projectallocationservice.models.*;
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
@RequestMapping("/api/v1")
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
    @GetMapping("/openings")
    @Operation(summary = "Get openings", description = "Retrieve a list of openings with optional pagination and filters")
    @ApiResponse(responseCode = "200", description = "Openings retrieved successfully", content = @Content(schema = @Schema(implementation = OpeningsListResponse.class)))
    public ResponseEntity<OpeningsListResponse> getAllOpenings(@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Boolean appliedBySelf, @RequestParam(required = false) Boolean postedBySelf) {
        // Fetch all openings and return a list of opening models with pagination details
        Page<DBOpening> dbOpenings = openingService.getAllOpenings(pageSize, pageNumber, appliedBySelf, postedBySelf);
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
    @GetMapping("/projects/{projectId}/openings")
    @Operation(summary = "Get openings for a project", description = "Retrieve a list of openings for a specific project")
    @ApiResponse(responseCode = "200", description = "Openings retrieved successfully", content = @Content(schema = @Schema(implementation = List.class)))
    public ResponseEntity<List<Opening>> getAllOpeningsForProject(@PathVariable Long projectId) {
        // Fetch openings for a specific project and return a list of opening models
        List<DBOpening> dbOpenings = openingService.getAllOpeningsForProject(projectId);
        return ResponseEntity.ok(openingMapper.entityToModel(dbOpenings));
    }

    /**
     * Retrieves a specific opening by its ID.
     *
     * @param id The ID of the opening.
     * @return A response containing the details of the specified opening.
     * @throws OpeningNotFoundException If the opening with the given ID is not found.
     */
    // Get a specific opening by ID
    @GetMapping("/openings/{id}")
    @Operation(summary = "Get opening by ID", description = "Retrieve a specific opening by its ID")
    @ApiResponse(responseCode = "200", description = "Opening retrieved successfully", content = @Content(schema = @Schema(implementation = Opening.class)))
    public ResponseEntity<Opening> getOpeningById(@PathVariable("id") Long id) {
        // Fetch an opening by its ID and return it as a model
        DBOpening dbOpening = openingService.getOpeningById(id);
        if (dbOpening != null) {
            return ResponseEntity.ok(openingMapper.entityToModel(dbOpening));
        } else {
            throw new OpeningNotFoundException(id);
        }
    }

    /**
     * Creates a new opening for a specific project.
     *
     * @param dbOpening  The opening details.
     * @param projectId  The ID of the project.
     * @return A response containing the details of the created opening.
     * @throws UnauthorizedAccessException If the user does not have permission to create an opening.
     * @throws SkillNotFoundException      If a skill associated with the opening is not found.
     */
    @PostMapping("/projects/{projectId}/openings")
    @Operation(summary = "Create opening", description = "Create a new opening for a specific project")
    @ApiResponse(responseCode = "201", description = "Opening created successfully", content = @Content(schema = @Schema(implementation = Opening.class)))
    public ResponseEntity<Opening> createOpening(@RequestBody @Validated DBOpening dbOpening, @PathVariable Long projectId) {
        // Create an audit log for creating an opening
        DBAuditLog auditLog = auditLogService.createAuditLog(
                DBAuditLog.builder()
                        .action("Create Opening for project " + projectId)
                        .user(DBUser.builder().id(requestContext.getLoggedinUser().getId()).build())
                        .loggedAt(new Date())
                        .auditComments(new ArrayList<>())
                        .build());
        auditCommentService.createAuditComment(DBAuditComment.builder()
                        .comment("Checking user permissions")
                        .auditLog(auditLog)
                .build());
        // Check user permissions to create an opening
        if (requestContext.getPermissions() == null || !requestContext.getPermissions().contains(PermissionName.CREATE_OPENING.toString())) {
            auditCommentService.createAuditComment(DBAuditComment.builder()
                    .comment("Unauthorized user trying to create opening")
                    .auditLog(auditLog)
                    .build());
            throw new UnauthorizedAccessException("You don't have permission to create an opening.");
        }
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Permissions passed")
                .auditLog(auditLog)
                .build());
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Checking if opening is duplicate")
                .auditLog(auditLog)
                .build());
        // Check if a duplicate opening with the same attributes already exists for the given project
        if (openingService.isDuplicateOpening(dbOpening, projectId)) {
            throw new OpeningAlreadyExistsException("An opening with the same attributes already exists for the project.");
        }
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Opening is unique")
                .auditLog(auditLog)
                .build());
        // Map the opening to a project and validate skills
        dbOpening.setProject(projectService.getProjectById(projectId));
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Project mapped to opening")
                .auditLog(auditLog)
                .build());
        // Retrieve the Skill entities from the database using the provided skillIds
        List<DBSkill> skills = new ArrayList<>();
        for (DBSkill skill : dbOpening.getSkills()) {
            skills.add(skillService.getSkillById(skill.getId())
                    .orElseThrow(() -> new SkillNotFoundException("Skill not found with ID: " + skill.getId())));
        }
        dbOpening.setSkills(skills);
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Skills assigned to opening")
                .auditLog(auditLog)
                .build());
        // Set the opening status, create it, and return the response
        dbOpening.setStatus(OpeningStatus.ACTIVE);
        DBOpening dbCreatedOpening = openingService.createOpening(dbOpening);
        Opening createdOpening = openingMapper.entityToModel(dbCreatedOpening);
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Opening created")
                .auditLog(auditLog)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOpening);
    }

    /**
     * Updates the details of an existing opening.
     *
     * @param id        The ID of the opening to update.
     * @param dbOpening The updated opening details.
     * @return A response containing the updated opening details.
     * @throws UnauthorizedAccessException If the user does not have permission to update an opening.
     * @throws OpeningNotFoundException    If the opening with the given ID is not found.
     * @throws OpeningUpdateException      If the update of the opening fails.
     */
    @PutMapping("/openings/{id}")
    @Operation(summary = "Update opening", description = "Update the details of an existing opening")
    @ApiResponse(responseCode = "200", description = "Opening updated successfully", content = @Content(schema = @Schema(implementation = Opening.class)))
    public ResponseEntity<Opening> updateOpening(
            @PathVariable("id") Long id,
            @RequestBody DBOpening dbOpening) {
        // Create an audit log for updating an opening
        DBAuditLog auditLog = auditLogService.createAuditLog(
                DBAuditLog.builder()
                        .action("Update Opening " + id)
                        .user(DBUser.builder().id(requestContext.getLoggedinUser().getId()).build())
                        .loggedAt(new Date())
                        .auditComments(new ArrayList<>())
                        .build());
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Checking user permissions")
                .auditLog(auditLog)
                .build());
        // Check user permissions to update an opening
        if (requestContext.getPermissions() == null || !requestContext.getPermissions().contains(PermissionName.MANAGE_OPENINGS.toString())) {
            auditCommentService.createAuditComment(DBAuditComment.builder()
                    .comment("Unauthorized user trying to create opening")
                    .auditLog(auditLog)
                    .build());
            throw new UnauthorizedAccessException("You don't have permission to create an opening.");
        }
        // Fetch the existing opening and update its properties
        DBOpening existingOpening = openingService.getOpeningById(id);
        if (existingOpening == null) {
            throw new OpeningNotFoundException(id);
        }
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Opening with " + id + " found")
                .auditLog(auditLog)
                .build());
        // Update the properties of the existingOpening with the new values from the request payload
        existingOpening.setTitle(dbOpening.getTitle());
        existingOpening.setDetails(dbOpening.getDetails());
        existingOpening.setLevel(dbOpening.getLevel());
        existingOpening.setLocation(dbOpening.getLocation());
        existingOpening.setStatus(dbOpening.getStatus());
        existingOpening.setSkills(dbOpening.getSkills());
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Updated opening with new values")
                .auditLog(auditLog)
                .build());
        // Save the updated opening
        DBOpening updatedOpening = openingService.updateOpening(id, existingOpening);
        if (updatedOpening == null) {
            throw new OpeningUpdateException("Failed to update the opening with ID: " + id);
        }
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Opening updated")
                .auditLog(auditLog)
                .build());
        // Return the updated opening in the response
        Opening responseOpening = openingMapper.entityToModel(updatedOpening);
        return ResponseEntity.ok(responseOpening);
    }

    // Update opening status
    @PatchMapping("/openings/{id}/status")
    @Operation(summary = "Update opening status", description = "Update the status of an existing opening")
    @ApiResponse(responseCode = "200", description = "Opening status updated successfully", content = @Content(schema = @Schema(implementation = Opening.class)))
    public ResponseEntity<Opening> updateOpeningStatus(@PathVariable Long id, @RequestParam OpeningStatus newStatus) {
        // Fetch the opening by ID and update its status
        DBOpening opening = openingService.getOpeningById(id);
        if (opening != null) {
            OpeningStatus currentStatus = opening.getStatus();

            if (currentStatus == OpeningStatus.ACTIVE) {
                if (newStatus == OpeningStatus.PENDING || newStatus == OpeningStatus.CLOSED) {
                    opening.setStatus(newStatus);
                    DBOpening dbUpdatedOpening = openingService.updateOpening(id, opening);
                    return ResponseEntity.ok(openingMapper.entityToModel(dbUpdatedOpening));
                }
            } else if (currentStatus == OpeningStatus.PENDING) {
                if (newStatus == OpeningStatus.ACTIVE || newStatus == OpeningStatus.CLOSED) {
                    opening.setStatus(newStatus);
                    DBOpening dbUpdatedOpening = openingService.updateOpening(id, opening);
                    return ResponseEntity.ok(openingMapper.entityToModel(dbUpdatedOpening));
                }
            } else if (currentStatus == OpeningStatus.CLOSED) {
                if (newStatus == OpeningStatus.CLOSED) {
                    opening.setStatus(newStatus);
                    DBOpening dbUpdatedOpening = openingService.updateOpening(id, opening);
                    return ResponseEntity.ok(openingMapper.entityToModel(dbUpdatedOpening));
                }
            }

            // If the requested status transition is not allowed, return bad request
            return ResponseEntity.badRequest().build();
        }
        throw new OpeningNotFoundException(id);
    }


    // Delete an opening
    @DeleteMapping("/openings/{id}")
    @Operation(summary = "Delete opening", description = "Delete an existing opening by its ID")
    @ApiResponse(responseCode = "204", description = "Opening deleted successfully")
    public ResponseEntity<Void> deleteOpening(@PathVariable("id") Long id) {
        // Delete the opening by ID and return the appropriate response
        boolean deleted = openingService.deleteOpening(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            throw new OpeningNotFoundException(id);
        }
    }
}

