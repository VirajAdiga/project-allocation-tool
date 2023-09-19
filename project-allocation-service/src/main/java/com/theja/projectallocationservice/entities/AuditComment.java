package com.theja.projectallocationservice.entities;

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
public class AuditComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Unique identifier for the audit comment

    private String comment;  // Textual comment associated with the audit log

    @ManyToOne(optional = false)
    @JoinColumn(name="audit_log_id")
    private AuditLog auditLog;  // The audit log to which this comment belongs
}
