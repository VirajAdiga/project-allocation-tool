package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.exceptions.DatabaseAccessException;
import com.theja.projectallocationservice.exceptions.ResourceNotFoundException;
import com.theja.projectallocationservice.entities.AuditComment;
import com.theja.projectallocationservice.exceptions.ServerSideGeneralException;
import com.theja.projectallocationservice.repositories.AuditCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        try {
            return auditCommentRepository.findAll();
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }

    /**
     * Retrieves an audit comment by its ID.
     *
     * @param id The ID of the audit comment to retrieve.
     * @return The audit comment with the specified ID.
     * @throws ResourceNotFoundException If the audit comment with the given ID is not found.
     */
    public AuditComment getAuditCommentById(Long id) {
        Optional<AuditComment> auditComment;
        try {
            auditComment = auditCommentRepository.findById(id);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
        if (auditComment.isEmpty()){
            throw new ResourceNotFoundException("Audit comment not found " + id);
        }
        return auditComment.get();
    }

    /**
     * Retrieves audit comments by their associated audit log ID.
     *
     * @param auditLogId The ID of the audit log.
     * @return List of audit comments associated with the specified audit log.
     */
    public List<AuditComment> getAuditCommentsByAuditLogId(Long auditLogId) {
        try {
            return auditCommentRepository.findByAuditLogId(auditLogId);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }

    /**
     * Creates a new audit comment.
     *
     * @param auditComment The audit comment to create.
     * @return The created audit comment.
     */
    public AuditComment createAuditComment(AuditComment auditComment) {
        try {
            return auditCommentRepository.save(auditComment);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }
}
