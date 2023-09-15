package com.theja.projectallocationservice.validation;

import com.theja.projectallocationservice.exceptions.SkillNotFoundException;
import com.theja.projectallocationservice.exceptions.UnauthorizedAccessException;
import com.theja.projectallocationservice.models.*;
import com.theja.projectallocationservice.services.AuditCommentService;
import com.theja.projectallocationservice.services.AuditLogService;
import com.theja.projectallocationservice.services.ProjectService;
import com.theja.projectallocationservice.services.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OpeningValidationService {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private SkillService skillService;
    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    private AuditCommentService auditCommentService;

    public void validateCreateOpening(DBOpening dbOpening, Long projectId, RequestContext requestContext) {
        DBAuditLog auditLog = createAuditLog(projectId, requestContext);

        checkUserPermissions(requestContext, auditLog);

        assignProjectToOpening(dbOpening, projectId, auditLog);
        assignSkillsToOpening(dbOpening, auditLog);
        dbOpening.setStatus(OpeningStatus.ACTIVE);

        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Opening validated")
                .auditLog(auditLog)
                .build());
    }

    private DBAuditLog createAuditLog(Long projectId, RequestContext requestContext) {
        DBAuditLog auditLog = auditLogService.createAuditLog(
                DBAuditLog.builder()
                        .action("Create Opening for project " + projectId)
                        .user(DBUser.builder().id(requestContext.getLoggedinUser().getId()).build())
                        .loggedAt(new Date())
                        .auditComments(new ArrayList<>())
                        .build());
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Checking user permissions")
                .auditLog(auditLog)
                .build());
        return auditLog;
    }

    private void checkUserPermissions(RequestContext requestContext, DBAuditLog auditLog) {
        if (requestContext.getPermissions() == null || !requestContext.getPermissions().contains(PermissionName.CREATE_OPENING.toString())) {
            auditCommentService.createAuditComment(DBAuditComment.builder()
                    .comment("Unauthorized user trying to create opening")
                    .auditLog(auditLog)
                    .build());
            throw new UnauthorizedAccessException("You don't have permission to create an opening.");
        }
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Permissions passed")
                .auditLog(auditLog)
                .build());
    }

    private void assignProjectToOpening(DBOpening dbOpening, Long projectId, DBAuditLog auditLog) {
        dbOpening.setProject(projectService.getProjectById(projectId));
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Project mapped to opening")
                .auditLog(auditLog)
                .build());
    }

    private void assignSkillsToOpening(DBOpening dbOpening, DBAuditLog auditLog) {
        List<DBSkill> skills = new ArrayList<>();
        for (DBSkill skill : dbOpening.getSkills()) {
            skills.add(skillService.getSkillById(skill.getId())
                    .orElseThrow(() -> new SkillNotFoundException("Skill not found with ID: " + skill.getId())));
        }
        dbOpening.setSkills(skills);
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Skills assigned to opening")
                .auditLog(auditLog)
                .build());
    }
}
