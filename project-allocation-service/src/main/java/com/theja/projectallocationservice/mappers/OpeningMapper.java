package com.theja.projectallocationservice.mappers;

import com.theja.projectallocationservice.models.DBOpening;
import com.theja.projectallocationservice.models.Opening;
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
    private UserMapper userMapper;

    // Convert a list of DBOpening entities to a list of Opening model objects
    public List<Opening> entityToModel(List<DBOpening> dbOpenings) {
        return dbOpenings.stream().map(this::entityToModel).collect(Collectors.toList());
    }

    // Convert a DBOpening entity to an Opening model object
    public Opening entityToModel(DBOpening dbOpening) {
        // Map the attributes of the DBOpening entity to the corresponding attributes of the Opening model
        // Note that the last argument (interviews) is set to null; this may be intentionally left for further processing
        return new Opening(dbOpening.getId(), dbOpening.getTitle(), dbOpening.getDetails(), dbOpening.getLevel(), dbOpening.getLocation(), dbOpening.getStatus(), userMapper.entityToPublicModel(dbOpening.getRecruiter()), projectMapper.entityToModel(dbOpening.getProject()), null, skillMapper.entityToModel(dbOpening.getSkills()));
    }
}
