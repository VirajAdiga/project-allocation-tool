package com.theja.projectallocationservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a database entity for storing audit logs and associated audit comments.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "audit_logs")
public class DBAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Unique identifier for the audit log

    private String action;  // Describes the action being audited

    @Column(name = "logged_at")
    private Date loggedAt;  // Timestamp of when the audit log was created

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private DBUser user;  // The user associated with the action being audited

    @OneToMany(mappedBy="auditLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DBAuditComment> auditComments = new ArrayList<>();  // List of comments associated with the audit log
}
