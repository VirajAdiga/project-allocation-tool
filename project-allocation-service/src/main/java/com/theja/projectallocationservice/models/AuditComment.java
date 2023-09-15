package com.theja.projectallocationservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Represents a comment associated with an audit log entry.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditComment {
    private Long id;             // Unique identifier for the audit comment
    private String comment;      // The content of the audit comment
    private AuditLog auditLog;   // The audit log entry to which this comment is associated
}
