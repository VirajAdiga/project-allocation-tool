package com.theja.projectallocationservice.mappers;

import com.theja.projectallocationservice.models.*;
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
    public List<Interview> entityToModel(List<DBInterview> dbInterviews) {
        return dbInterviews.stream().map(this::entityToModel).collect(Collectors.toList());
    }

    // Convert a DBInterview entity to an Interview model object
    public Interview entityToModel(DBInterview dbInterview) {
        // Map the attributes of the DBInterview entity to the corresponding attributes of the Interview model
        // Note that the last argument (auditComments) is set to null; this may be intentionally left for further processing
        return new Interview(
                dbInterview.getId(),
                dbInterview.getTitle(),
                userMapper.entityToPublicModel(dbInterview.getInterviewer()),
                dbInterview.getStatus(),
                dbInterview.getFeedback(),
                dbInterview.getScheduledTime(),
                applicationMapper.entityToModel(dbInterview.getApplication())
        );
    }
}
