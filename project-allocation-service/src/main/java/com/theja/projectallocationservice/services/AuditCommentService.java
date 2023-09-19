package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.exceptions.ResourceNotFoundException;
import com.theja.projectallocationservice.entities.AuditComment;
import com.theja.projectallocationservice.repositories.AuditCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing audit comment-related operations.
 */
@Service
public class AuditCommentService {
    @Autowired
    private AuditCommentRepository auditCommentRepository;

    /**
     * Retrieves a list of all audit comments.
     *
     * @return List of all audit comments.
     */
    public List<AuditComment> getAllAuditComments() {
        return auditCommentRepository.findAll();
    }

    /**
     * Retrieves an audit comment by its ID.
     *
     * @param id The ID of the audit comment to retrieve.
     * @return The audit comment with the specified ID.
     * @throws ResourceNotFoundException If the audit comment with the given ID is not found.
     */
    public AuditComment getAuditCommentById(Long id) {
        return auditCommentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Audit comment not found"));
    }

    /**
     * Retrieves audit comments by their associated audit log ID.
     *
     * @param auditLogId The ID of the audit log.
     * @return List of audit comments associated with the specified audit log.
     */
    public List<AuditComment> getAuditCommentsByAuditLogId(Long auditLogId) {
        return auditCommentRepository.findByAuditLogId(auditLogId);
    }

    /**
     * Creates a new audit comment.
     *
     * @param auditComment The audit comment to create.
     * @return The created audit comment.
     */
    public AuditComment createAuditComment(AuditComment auditComment) {
        return auditCommentRepository.save(auditComment);
    }
}
