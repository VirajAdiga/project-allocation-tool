package com.theja.projectallocationservice.repositories;

import com.theja.projectallocationservice.entities.AuditComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing database operations related to audit comments.
 */
@Repository
public interface AuditCommentRepository extends JpaRepository<AuditComment, Long> {

    /**
     * Find audit comments by audit log ID.
     *
     * @param auditLogId The ID of the audit log associated with the audit comments.
     * @return A list of audit comments associated with the specified audit log ID.
     */
    List<AuditComment> findByAuditLogId(Long auditLogId);
}
