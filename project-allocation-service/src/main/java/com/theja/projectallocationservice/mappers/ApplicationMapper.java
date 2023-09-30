package com.theja.projectallocationservice.mappers;

import com.theja.projectallocationservice.entities.Application;
import com.theja.projectallocationservice.services.UserServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApplicationMapper {
    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private OpeningMapper openingMapper;

    // Convert a list of DBApplication entities to a list of Application model objects
    public List<com.theja.projectallocationservice.dto.Application> entityToModel(List<Application> applications) {
        return applications.stream().map(this::entityToModel).collect(Collectors.toList());
    }

    // Convert a DBApplication entity to an Application model object
    public com.theja.projectallocationservice.dto.Application entityToModel(Application application) {
        // Map the attributes of the DBApplication entity to the corresponding attributes of the Application model
        return new com.theja.projectallocationservice.dto.Application(application.getId(), application.getStatus(), application.getAppliedAt(), userServiceClient.getUserById(application.getCandidateId()), openingMapper.entityToModel(application.getOpening()), null);
    }
}
