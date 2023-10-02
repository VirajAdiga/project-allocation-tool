package com.theja.projectallocationservice.controllers;

import com.theja.projectallocationservice.dto.AuditLogResponse;
import com.theja.projectallocationservice.mappers.AuditCommentMapper;
import com.theja.projectallocationservice.mappers.AuditLogMapper;
import com.theja.projectallocationservice.entities.*;
import com.theja.projectallocationservice.repositories.AuditCommentRepository;
import com.theja.projectallocationservice.services.AuditCommentService;
import com.theja.projectallocationservice.services.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The `AuditLogController` class handles HTTP requests related to audit logs.
 * It provides an endpoint for retrieving a paginated list of audit logs.
 */
@RestController
@RequestMapping("/api/v1/audit-logs")
@Tag(name = "Audit Log Controller", description = "Endpoints related to audit log management")
public class AuditLogController {
    // Autowired fields for services and mappers...
    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    private AuditLogMapper auditLogMapper;
    @Autowired
    private AuditCommentRepository auditCommentRepository;
    @Autowired
    private AuditCommentService auditCommentService;
    @Autowired
    private AuditCommentMapper auditCommentMapper;

    /**
     * Retrieves a paginated list of audit logs.
     *
     * @param pageSize   The number of audit logs to include in each page.
     * @param pageNumber The page number of audit logs to retrieve.
     * @return A response containing a paginated list of model audit logs and total element count.
     */
    @GetMapping("")
    @Operation(summary = "Get all audit logs", description = "Retrieve a paginated list of audit logs")
    @ApiResponse(responseCode = "200", description = "Audit logs retrieved successfully", content = @Content(schema = @Schema(implementation = AuditLogResponse.class)))
    public ResponseEntity<AuditLogResponse> getAllAuditLogs(@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageNumber) {
        // Fetch a paginated list of audit logs
        Page<AuditLog> dbAuditLogs = auditLogService.getAllAuditLogs(pageSize, pageNumber);
        // Convert entity audit logs to model audit logs using the mapper
        // Build a response containing model audit logs and total element count
        AuditLogResponse response = AuditLogResponse.builder()
                .auditLogs(auditLogMapper.entityToModel(dbAuditLogs.getContent()))
                .totalElements(dbAuditLogs.getTotalElements())
                .build();
        // Return the response in the ResponseEntity with an OK status
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a list of audit comments associated with a specific audit log ID.
     *
     * @param auditLogId The ID of the audit log.
     * @return A response containing a list of audit comments related to the specified audit log.
     */
    @GetMapping("/{auditLogId}/comments")
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
