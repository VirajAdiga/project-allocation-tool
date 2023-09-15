package com.theja.projectallocationservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a database entity for storing audit comments associated with audit logs.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "audit_comments")
public class DBAuditComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Unique identifier for the audit comment

    private String comment;  // Textual comment associated with the audit log

    @ManyToOne(optional = false)
    @JoinColumn(name="audit_log_id")
    private DBAuditLog auditLog;  // The audit log to which this comment belongs
}
