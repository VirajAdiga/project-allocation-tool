package com.theja.projectallocationservice.mappers;

import com.theja.projectallocationservice.entities.AuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuditLogMapper {
    @Autowired
    private UserMapper userMapper;

    // Convert a list of DBAuditLog entities to a list of AuditLog model objects
    public List<com.theja.projectallocationservice.dto.AuditLog> entityToModel(List<AuditLog> auditLogs) {
        return auditLogs.stream().map(this::entityToModel).collect(Collectors.toList());
    }

    // Convert a DBAuditLog entity to an AuditLog model object
    public com.theja.projectallocationservice.dto.AuditLog entityToModel(AuditLog auditLog) {
        // Map the attributes of the DBAuditLog entity to the corresponding attributes of the AuditLog model
        // Note that the last argument (auditComments) is set to null; this may be intentionally left for further processing
        return new com.theja.projectallocationservice.dto.AuditLog(
                auditLog.getId(),
                auditLog.getAction(),
                auditLog.getLoggedAt(),
                userMapper.entityToPublicModel(auditLog.getUser()),
                null
        );
    }
}
