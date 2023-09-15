package com.theja.projectallocationservice.mappers;

import com.theja.projectallocationservice.models.DBProject;
import com.theja.projectallocationservice.models.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectMapper {

    @Autowired
    private UserMapper userMapper;

    // Convert a list of DBProject entities to a list of Project model objects
    public List<Project> entityToModel(List<DBProject> dbProjects) {
        return dbProjects.stream().map(this::entityToModel).collect(Collectors.toList());
    }

    // Convert a DBProject entity to a Project model object
    public Project entityToModel(DBProject dbProject) {
        // Map the attributes of the DBProject entity to the corresponding attributes of the Project model
        // Note that the last argument (openings) is set to null; this may be intentionally left for further processing
        return new Project(
                dbProject.getId(),
                dbProject.getTitle(),
                dbProject.getDetails(),
                userMapper.entityToModel(dbProject.getAllocatedUsers()),
                null
        );
    }
}
