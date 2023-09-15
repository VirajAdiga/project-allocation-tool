package com.theja.projectallocationservice.mappers;

import com.theja.projectallocationservice.models.AuditComment;
import com.theja.projectallocationservice.models.DBAuditComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuditCommentMapper {
    @Autowired
    private AuditLogMapper auditLogMapper;

    // Convert a list of DBAuditComment entities to a list of AuditComment model objects
    public List<AuditComment> entityToModel(List<DBAuditComment> dbAuditComments) {
        return dbAuditComments.stream().map(this::entityToModel).collect(Collectors.toList());
    }

    // Convert a DBAuditComment entity to an AuditComment model object
    public AuditComment entityToModel(DBAuditComment dbAuditComment) {
        // Map the attributes of the DBAuditComment entity to the corresponding attributes of the AuditComment model
        return new AuditComment(dbAuditComment.getId(), dbAuditComment.getComment(), auditLogMapper.entityToModel(dbAuditComment.getAuditLog()));
    }
}
