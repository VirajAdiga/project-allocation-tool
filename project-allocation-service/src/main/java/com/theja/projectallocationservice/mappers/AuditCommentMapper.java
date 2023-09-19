package com.theja.projectallocationservice.mappers;

import com.theja.projectallocationservice.entities.AuditComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuditCommentMapper {
    @Autowired
    private AuditLogMapper auditLogMapper;

    // Convert a list of DBAuditComment entities to a list of AuditComment model objects
    public List<com.theja.projectallocationservice.dto.AuditComment> entityToModel(List<AuditComment> auditComments) {
        return auditComments.stream().map(this::entityToModel).collect(Collectors.toList());
    }

    // Convert a DBAuditComment entity to an AuditComment model object
    public com.theja.projectallocationservice.dto.AuditComment entityToModel(AuditComment auditComment) {
        // Map the attributes of the DBAuditComment entity to the corresponding attributes of the AuditComment model
        return new com.theja.projectallocationservice.dto.AuditComment(auditComment.getId(), auditComment.getComment(), auditLogMapper.entityToModel(auditComment.getAuditLog()));
    }
}
