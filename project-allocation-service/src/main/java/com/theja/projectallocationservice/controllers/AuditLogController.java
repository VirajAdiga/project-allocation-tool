package com.theja.projectallocationservice.controllers;

import com.theja.projectallocationservice.dto.AuditLogResponse;
import com.theja.projectallocationservice.mappers.AuditLogMapper;
import com.theja.projectallocationservice.entities.*;
import com.theja.projectallocationservice.services.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The `AuditLogController` class handles HTTP requests related to audit logs.
 * It provides an endpoint for retrieving a paginated list of audit logs.
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Audit Log Controller", description = "Endpoints related to audit log management")
public class AuditLogController {
    // Autowired fields for services and mappers...
    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    private AuditLogMapper auditLogMapper;

    /**
     * Retrieves a paginated list of audit logs.
     *
     * @param pageSize   The number of audit logs to include in each page.
     * @param pageNumber The page number of audit logs to retrieve.
     * @return A response containing a paginated list of model audit logs and total element count.
     */
    @GetMapping("/audit-logs")
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
}
