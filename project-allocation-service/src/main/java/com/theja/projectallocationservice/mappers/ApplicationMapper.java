package com.theja.projectallocationservice.mappers;

import com.theja.projectallocationservice.models.Application;
import com.theja.projectallocationservice.models.DBApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApplicationMapper {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OpeningMapper openingMapper;

    // Convert a list of DBApplication entities to a list of Application model objects
    public List<Application> entityToModel(List<DBApplication> dbApplications) {
        return dbApplications.stream().map(this::entityToModel).collect(Collectors.toList());
    }

    // Convert a DBApplication entity to an Application model object
    public Application entityToModel(DBApplication dbApplication) {
        // Map the attributes of the DBApplication entity to the corresponding attributes of the Application model
        return new Application(dbApplication.getId(), dbApplication.getStatus(), dbApplication.getAppliedAt(), userMapper.entityToPublicModel(dbApplication.getCandidate()), openingMapper.entityToModel(dbApplication.getOpening()), null);
    }
}
