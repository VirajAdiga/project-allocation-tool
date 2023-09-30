package com.theja.projectallocationservice.controllers;

import com.theja.projectallocationservice.dto.CreateProjectDTO;
import com.theja.projectallocationservice.dto.ProjectListResponse;
import com.theja.projectallocationservice.dto.RequestContext;
import com.theja.projectallocationservice.entities.enums.PermissionName;
import com.theja.projectallocationservice.exceptions.ResourceNotFoundException;
import com.theja.projectallocationservice.exceptions.UnauthorizedAccessException;
import com.theja.projectallocationservice.mappers.ProjectMapper;
import com.theja.projectallocationservice.entities.*;
import com.theja.projectallocationservice.services.AuditCommentService;
import com.theja.projectallocationservice.services.AuditLogService;
import com.theja.projectallocationservice.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The `ProjectController` class manages HTTP requests related to projects.
 * It provides endpoints for retrieving, creating, updating, and managing project details.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Projects", description = "Endpoints related to project management")
public class ProjectController {
    // Autowired fields for services, mappers, and context...
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private RequestContext requestContext;
    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    private AuditCommentService auditCommentService;

    /**
     * Retrieves a paginated list of projects.
     *
     * @param pageSize   The number of projects per page.
     * @param pageNumber The page number of projects to retrieve.
     * @return The response containing a list of project models with pagination details.
     */
    // Get all projects
    @GetMapping("/projects")
    @Operation(summary = "Get all projects", description = "Retrieve a paginated list of projects")
    @ApiResponse(responseCode = "200", description = "Projects retrieved successfully", content = @Content(schema = @Schema(implementation = ProjectListResponse.class)))
    public ResponseEntity<ProjectListResponse> getAllProjects(@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageNumber) {
        log.info("Request reached projects controller: with page size "+ pageSize + " with page number " + pageNumber);
        // Fetch all projects and return a list of project models with pagination details
        Page<Project> dbProjects = projectService.getAllProjects(pageSize, pageNumber);
        ProjectListResponse response = ProjectListResponse.builder()
                .projects(projectMapper.entityToModel(dbProjects.getContent()))
                .totalElements(dbProjects.getTotalElements())
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a list of all projects without pagination.
     *
     * @return The response containing a list of project models.
     */
    @GetMapping("/projects/all")
    @Operation(summary = "Get all projects without pagination", description = "Retrieve a list of all projects without pagination")
    @ApiResponse(responseCode = "200", description = "Projects retrieved successfully", content = @Content(schema = @Schema(implementation = List.class)))
    public ResponseEntity<List<com.theja.projectallocationservice.dto.Project>> getAllProjects() {
        // Fetch all projects without pagination and return a list of project models
        List<Project> projects = projectService.getAllProjectsWithoutPagination();
        return ResponseEntity.ok(projectMapper.entityToModel(projects));
    }

    /**
     * Retrieves a list of projects associated with a user.
     *
     * @param userId The ID of the user to fetch projects for.
     * @return The response containing a list of project models.
     */
    @GetMapping("/projects/users/{userId}")
    @Operation(summary = "Get projects for a user", description = "Retrieve a list of projects associated with a user")
    @ApiResponse(responseCode = "200", description = "Projects retrieved successfully", content = @Content(schema = @Schema(implementation = List.class)))
    public ResponseEntity<List<com.theja.projectallocationservice.dto.Project>> getProjectsForUser(@PathVariable Long userId) {
        // Fetch projects associated with a user and return a list of project models
        List<Project> projects = projectService.getProjectsForUser(userId);
        return ResponseEntity.ok(projectMapper.entityToModel(projects));
    }

    /**
     * Retrieves a specific project by its ID.
     *
     * @param id The ID of the project to retrieve.
     * @return The project model corresponding to the provided project ID.
     */
    // Get a specific project by ID
    @GetMapping("/projects/{id}")
    @Operation(summary = "Get project by ID", description = "Retrieve a specific project by its ID")
    @ApiResponse(responseCode = "200", description = "Project retrieved successfully", content = @Content(schema = @Schema(implementation = com.theja.projectallocationservice.dto.Project.class)))
    public ResponseEntity<com.theja.projectallocationservice.dto.Project> getProjectById(@PathVariable("id") Long id) {
        // Fetch a project by its ID and return it as a model
        Project project = projectService.getProjectById(id);
        if (project != null) {
            return ResponseEntity.ok(projectMapper.entityToModel(project));
        } else {
            throw new ResourceNotFoundException("Project not found with ID: " + id);
        }
    }

    /**
     * Creates a new project.
     *
     * @param projectDTO The project details to be created.
     * @return The created project model in the response.
     */
    // Create a new project
    @PostMapping("/projects")
    @Operation(summary = "Create project", description = "Create a new project")
    @ApiResponse(responseCode = "201", description = "Project created successfully", content = @Content(schema = @Schema(implementation = com.theja.projectallocationservice.dto.Project.class)))
    public ResponseEntity<com.theja.projectallocationservice.dto.Project> createProject(@RequestBody CreateProjectDTO projectDTO) {
        // Create an audit log for creating a project
        AuditLog auditLog = auditLogService.createAuditLog(
                AuditLog.builder()
                        .action("Creating project")
                        .userId(requestContext.getLoggedinUser().getId())
                        .loggedAt(new Date())
                        .auditComments(new ArrayList<>())
                        .build());
        auditCommentService.createAuditComment(AuditComment.builder()
                .comment("Checking user permissions")
                .auditLog(auditLog)
                .build());
        // Check user permissions to create a project
        if (requestContext.getPermissions() == null || !requestContext.getPermissions().contains(PermissionName.CREATE_PROJECT.toString())) {
            auditCommentService.createAuditComment(AuditComment.builder()
                    .comment("Unauthorized user trying to create project")
                    .auditLog(auditLog)
                    .build());
            throw new UnauthorizedAccessException("You don't have permission to create a project.");
        }
        auditCommentService.createAuditComment(AuditComment.builder()
                .comment("Permissions passed")
                .auditLog(auditLog)
                .build());
        // Create the project, save it, and return the response
        Project dbCreatedProject = projectService.createProject(projectDTO);
        auditCommentService.createAuditComment(AuditComment.builder()
                .comment("Project created")
                .auditLog(auditLog)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).body(projectMapper.entityToModel(dbCreatedProject));
    }

    /**
     * Creates a new project.
     *
     * @param project The project details to be created.
     * @return The created project model in the response.
     */
    // Update an existing project
    @PutMapping("/projects/{id}")
    @Operation(summary = "Update project", description = "Update the details of an existing project")
    @ApiResponse(responseCode = "200", description = "Project updated successfully", content = @Content(schema = @Schema(implementation = com.theja.projectallocationservice.dto.Project.class)))
    public ResponseEntity<com.theja.projectallocationservice.dto.Project> updateProject(@PathVariable("id") Long id, @RequestBody Project project) {
        // Update an existing project by ID, save the changes, and return the response
        Project dbUpdatedProject = projectService.updateProject(id, project);
        if (dbUpdatedProject != null) {
            return ResponseEntity.ok(projectMapper.entityToModel(dbUpdatedProject));
        } else {
            throw new ResourceNotFoundException("Project not found with ID: " + id);
        }
    }

    /**
     * Deletes an existing project by its ID.
     *
     * @param id The ID of the project to delete.
     * @return The appropriate response based on the deletion status.
     */
    // Delete a project
    @DeleteMapping("/projects/{id}")
    @Operation(summary = "Delete project", description = "Delete an existing project by its ID")
    @ApiResponse(responseCode = "204", description = "Project deleted successfully")
    public ResponseEntity<Void> deleteProject(@PathVariable("id") Long id) {
        // Delete a project by ID and return the appropriate response
        boolean deleted = projectService.deleteProject(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFoundException("Project not found with ID: " + id);
        }
    }
}

