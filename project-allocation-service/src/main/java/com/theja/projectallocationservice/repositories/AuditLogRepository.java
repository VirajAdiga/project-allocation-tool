package com.theja.projectallocationservice.repositories;

import com.theja.projectallocationservice.models.DBAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing database operations related to audit logs.
 */
@Repository
public interface AuditLogRepository extends JpaRepository<DBAuditLog, Long> {

    /**
     * Find audit logs by user ID.
     *
     * @param userId The ID of the user associated with the audit logs.
     * @return A list of audit logs associated with the specified user ID.
     */
    List<DBAuditLog> findByUserId(Long userId);
}
