package com.theja.projectallocationservice.controllers;

import com.theja.projectallocationservice.mappers.AuditCommentMapper;
import com.theja.projectallocationservice.entities.*;
import com.theja.projectallocationservice.repositories.AuditCommentRepository;
import com.theja.projectallocationservice.services.AuditCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The `AuditCommentController` class handles HTTP requests related to audit comments associated with audit logs.
 * It provides an endpoint for retrieving audit comments based on a specific audit log ID.
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Audit Comment Controller", description = "Endpoints related to audit comment management")
public class AuditCommentController {
    // Autowired fields for repositories, services, and mappers...
    @Autowired
    private AuditCommentRepository auditCommentRepository;
    @Autowired
    private AuditCommentService auditCommentService;
    @Autowired
    private AuditCommentMapper auditCommentMapper;

    /**
     * Retrieves a list of audit comments associated with a specific audit log ID.
     *
     * @param auditLogId The ID of the audit log.
     * @return A response containing a list of audit comments related to the specified audit log.
     */
    @GetMapping("/audit-logs/{auditLogId}/comments")
    @Operation(summary = "Get audit comments by audit log ID", description = "Retrieve a list of audit comments associated with a specific audit log ID")
    @ApiResponse(responseCode = "200", description = "Audit comments retrieved successfully", content = @Content(schema = @Schema(implementation = com.theja.projectallocationservice.dto.AuditComment.class)))
    @ApiResponse(responseCode = "404", description = "Audit log not found")
    public ResponseEntity<List<com.theja.projectallocationservice.dto.AuditComment>> getAuditCommentsByAuditLogId(@PathVariable Long auditLogId) {
        // Fetch audit comments related to a specific audit log by ID
        List<AuditComment> auditComments = auditCommentService.getAuditCommentsByAuditLogId(auditLogId);
        // Convert entity audit comments to model audit comments using the mapper
        // Return the list of model audit comments in the response
        return new ResponseEntity<>(auditCommentMapper.entityToModel(auditComments), HttpStatus.OK);
    }
}
