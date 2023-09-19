package com.theja.projectallocationservice.mappers;

import com.theja.projectallocationservice.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InterviewMapper {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ApplicationMapper applicationMapper;

    // Convert a list of DBInterview entities to a list of Interview model objects
    public List<com.theja.projectallocationservice.dto.Interview> entityToModel(List<Interview> interviews) {
        return interviews.stream().map(this::entityToModel).collect(Collectors.toList());
    }

    // Convert a DBInterview entity to an Interview model object
    public com.theja.projectallocationservice.dto.Interview entityToModel(Interview interview) {
        // Map the attributes of the DBInterview entity to the corresponding attributes of the Interview model
        // Note that the last argument (auditComments) is set to null; this may be intentionally left for further processing
        return new com.theja.projectallocationservice.dto.Interview(
                interview.getId(),
                interview.getTitle(),
                userMapper.entityToPublicModel(interview.getInterviewer()),
                interview.getStatus(),
                interview.getFeedback(),
                interview.getScheduledTime(),
                applicationMapper.entityToModel(interview.getApplication())
        );
    }
}
