package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.entities.AuditLog;
import com.theja.projectallocationservice.exceptions.DatabaseAccessException;
import com.theja.projectallocationservice.exceptions.ResourceNotFoundException;
import com.theja.projectallocationservice.exceptions.ServerSideGeneralException;
import com.theja.projectallocationservice.repositories.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
        try {
            return auditLogRepository.findAll(pageRequest);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }

    /**
     * Retrieves all audit logs associated with a specific user.
     *
     * @param userId The ID of the user.
     * @return List of audit logs associated with the user.
     */
    public List<AuditLog> getAllAuditLogsForUser(Long userId) {
        try {
            return auditLogRepository.findByUserId(userId);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }

    /**
     * Retrieves an audit log by its ID.
     *
     * @param id The ID of the audit log to retrieve.
     * @return The audit log with the specified ID, or null if not found.
     */
    public AuditLog getAuditLogById(Long id) {
        Optional<AuditLog> auditLog;
        try {
            auditLog = auditLogRepository.findById(id);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
        if (auditLog.isEmpty()) {
            throw new ResourceNotFoundException("Audit log not found with id: " + id);
        }
        return auditLog.get();
    }

    /**
     * Creates a new audit log.
     *
     * @param auditLog The audit log to create.
     * @return The created audit log.
     */
    public AuditLog createAuditLog(AuditLog auditLog) {
        try {
            return auditLogRepository.save(auditLog);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }
}
