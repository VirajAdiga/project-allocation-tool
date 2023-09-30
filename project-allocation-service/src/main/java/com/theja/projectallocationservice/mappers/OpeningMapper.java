package com.theja.projectallocationservice.mappers;

import com.theja.projectallocationservice.entities.Opening;
import com.theja.projectallocationservice.services.UserServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OpeningMapper {
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private SkillMapper skillMapper;
    @Autowired
    private UserServiceClient userServiceClient;

    // Convert a list of DBOpening entities to a list of Opening model objects
    public List<com.theja.projectallocationservice.dto.Opening> entityToModel(List<Opening> openings) {
        return openings.stream().map(this::entityToModel).collect(Collectors.toList());
    }

    // Convert a DBOpening entity to an Opening model object
    public com.theja.projectallocationservice.dto.Opening entityToModel(Opening opening) {
        // Map the attributes of the DBOpening entity to the corresponding attributes of the Opening model
        // Note that the last argument (interviews) is set to null; this may be intentionally left for further processing
        return new com.theja.projectallocationservice.dto.Opening(opening.getId(), opening.getTitle(), opening.getDetails(), opening.getLevel(), opening.getLocation(), opening.getStatus(), userServiceClient.getUserById(opening.getRecruiterId()), projectMapper.entityToModel(opening.getProject()), null, skillMapper.entityToModel(opening.getSkills()));
    }
}
