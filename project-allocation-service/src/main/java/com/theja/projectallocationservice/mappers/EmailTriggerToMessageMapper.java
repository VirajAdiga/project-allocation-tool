package com.theja.projectallocationservice.mappers;

import com.theja.projectallocationservice.entities.enums.EmailTriggerActions;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EmailTriggerToMessageMapper {

    private final Map<EmailTriggerActions, String> emailTriggerActionsToMessageMap = new HashMap<>();

    public EmailTriggerToMessageMapper(){
        this.emailTriggerActionsToMessageMap.put(EmailTriggerActions.PROJECT_CREATION, "EMAIL_CODE_001");
        this.emailTriggerActionsToMessageMap.put(EmailTriggerActions.OPENING_CREATION, "EMAIL_CODE_002");
        this.emailTriggerActionsToMessageMap.put(EmailTriggerActions.APPLICATION_CREATION, "EMAIL_CODE_003");
        this.emailTriggerActionsToMessageMap.put(EmailTriggerActions.PROJECT_ALLOCATION, "EMAIL_CODE_004");
    }

    public String getEmailCode(EmailTriggerActions emailTriggerActions){
        return this.emailTriggerActionsToMessageMap.get(emailTriggerActions);
    }
}
