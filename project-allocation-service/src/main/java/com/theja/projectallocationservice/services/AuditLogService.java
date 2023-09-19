package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.entities.AuditLog;
import com.theja.projectallocationservice.repositories.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing audit log-related operations.
 */
@Service
public class AuditLogService {
    @Autowired
    private AuditLogRepository auditLogRepository;

    /**
     * Retrieves a paginated list of all audit logs.
     *
     * @param pageSize   The maximum number of audit logs per page.
     * @param pageNumber The page number to retrieve.
     * @return A page containing a list of audit logs.
     */
    public Page<AuditLog> getAllAuditLogs(Integer pageSize, Integer pageNumber) {
        if (pageSize == null) pageSize = 1000;
        if (pageNumber == null) pageNumber = 0;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return auditLogRepository.findAll(pageRequest);
    }

    /**
     * Retrieves all audit logs associated with a specific user.
     *
     * @param userId The ID of the user.
     * @return List of audit logs associated with the user.
     */
    public List<AuditLog> getAllAuditLogsForUser(Long userId) {
        return auditLogRepository.findByUserId(userId);
    }

    /**
     * Retrieves an audit log by its ID.
     *
     * @param id The ID of the audit log to retrieve.
     * @return The audit log with the specified ID, or null if not found.
     */
    public AuditLog getAuditLogById(Long id) {
        Optional<AuditLog> auditLog = auditLogRepository.findById(id);
        return auditLog.orElse(null);
    }

    /**
     * Creates a new audit log.
     *
     * @param auditLog The audit log to create.
     * @return The created audit log.
     */
    public AuditLog createAuditLog(AuditLog auditLog) {
        return auditLogRepository.save(auditLog);
    }
}
