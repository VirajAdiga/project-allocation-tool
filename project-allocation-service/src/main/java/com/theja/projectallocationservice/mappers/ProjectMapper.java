package com.theja.projectallocationservice.mappers;

import com.theja.projectallocationservice.entities.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectMapper {

    @Autowired
    private UserMapper userMapper;

    // Convert a list of DBProject entities to a list of Project model objects
    public List<com.theja.projectallocationservice.dto.Project> entityToModel(List<Project> projects) {
        return projects.stream().map(this::entityToModel).collect(Collectors.toList());
    }

    // Convert a DBProject entity to a Project model object
    public com.theja.projectallocationservice.dto.Project entityToModel(Project project) {
        // Map the attributes of the DBProject entity to the corresponding attributes of the Project model
        // Note that the last argument (openings) is set to null; this may be intentionally left for further processing
        return new com.theja.projectallocationservice.dto.Project(
                project.getId(),
                project.getTitle(),
                project.getDetails(),
                userMapper.entityToModel(project.getAllocatedUsers()),
                null
        );
    }
}
