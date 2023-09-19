package com.theja.projectallocationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Represents an audit log entry capturing actions performed in the system.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditLog {
    private Long id;                 // Unique identifier for the audit log entry
    private String action;           // Describes the action performed
    private Date loggedAt;           // The timestamp when the action was logged
    private PublicUser user;         // The user associated with the action
    private List<AuditComment> auditComments; // List of comments associated with this audit log entry
}
