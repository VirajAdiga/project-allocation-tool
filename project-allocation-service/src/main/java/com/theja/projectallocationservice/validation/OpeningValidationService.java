package com.theja.projectallocationservice.validation;

import com.theja.projectallocationservice.dto.RequestContext;
import com.theja.projectallocationservice.entities.enums.OpeningStatus;
import com.theja.projectallocationservice.entities.enums.PermissionName;
import com.theja.projectallocationservice.exceptions.SkillNotFoundException;
import com.theja.projectallocationservice.exceptions.UnauthorizedAccessException;
import com.theja.projectallocationservice.entities.*;
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

    public void validateCreateOpening(Opening opening, Long projectId, RequestContext requestContext) {
        AuditLog auditLog = createAuditLog(projectId, requestContext);

        checkUserPermissions(requestContext, auditLog);

        assignProjectToOpening(opening, projectId, auditLog);
        assignSkillsToOpening(opening, auditLog);
        opening.setStatus(OpeningStatus.ACTIVE);

        auditCommentService.createAuditComment(AuditComment.builder()
                .comment("Opening validated")
                .auditLog(auditLog)
                .build());
    }

    private AuditLog createAuditLog(Long projectId, RequestContext requestContext) {
        AuditLog auditLog = auditLogService.createAuditLog(
                AuditLog.builder()
                        .action("Create Opening for project " + projectId)
                        .user(User.builder().id(requestContext.getLoggedinUser().getId()).build())
                        .loggedAt(new Date())
                        .auditComments(new ArrayList<>())
                        .build());
        auditCommentService.createAuditComment(AuditComment.builder()
                .comment("Checking user permissions")
                .auditLog(auditLog)
                .build());
        return auditLog;
    }

    private void checkUserPermissions(RequestContext requestContext, AuditLog auditLog) {
        if (requestContext.getPermissions() == null || !requestContext.getPermissions().contains(PermissionName.CREATE_OPENING.toString())) {
            auditCommentService.createAuditComment(AuditComment.builder()
                    .comment("Unauthorized user trying to create opening")
                    .auditLog(auditLog)
                    .build());
            throw new UnauthorizedAccessException("You don't have permission to create an opening.");
        }
        auditCommentService.createAuditComment(AuditComment.builder()
                .comment("Permissions passed")
                .auditLog(auditLog)
                .build());
    }

    private void assignProjectToOpening(Opening opening, Long projectId, AuditLog auditLog) {
        opening.setProject(projectService.getProjectById(projectId));
        auditCommentService.createAuditComment(AuditComment.builder()
                .comment("Project mapped to opening")
                .auditLog(auditLog)
                .build());
    }

    private void assignSkillsToOpening(Opening opening, AuditLog auditLog) {
        List<Skill> skills = new ArrayList<>();
        for (Skill skill : opening.getSkills()) {
            skills.add(skillService.getSkillById(skill.getId())
                    .orElseThrow(() -> new SkillNotFoundException("Skill not found with ID: " + skill.getId())));
        }
        opening.setSkills(skills);
        auditCommentService.createAuditComment(AuditComment.builder()
                .comment("Skills assigned to opening")
                .auditLog(auditLog)
                .build());
    }
}
