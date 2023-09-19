package com.theja.projectallocationservice.dto;

import com.theja.projectallocationservice.dto.AuditLog;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Represents a response containing a list of audit log entries and the total number of elements.
 */
@Builder
@Getter
public class AuditLogResponse {
    List<AuditLog> auditLogs;    // List of audit log entries
    Long totalElements;          // Total number of audit log entries
}
