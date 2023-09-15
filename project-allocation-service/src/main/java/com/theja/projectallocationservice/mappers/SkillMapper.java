package com.theja.projectallocationservice.mappers;

import com.theja.projectallocationservice.models.DBSkill;
import com.theja.projectallocationservice.models.Skill;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SkillMapper {

    // Convert a list of DBSkill entities to a list of Skill model objects
    public List<Skill> entityToModel(List<DBSkill> dbSkills) {
        if (dbSkills == null) {
            return new ArrayList<>(); // Return an empty list if the input is null
        }
        // Map each DBSkill entity to its corresponding Skill model object using Java Stream API
        return dbSkills.stream().map(this::entityToModel).collect(Collectors.toList());
    }

    // Convert a DBSkill entity to a Skill model object
    public Skill entityToModel(DBSkill dbSkill) {
        // Map the attributes of the DBSkill entity to the corresponding attributes of the Skill model
        // The last two arguments (openings and candidates) are set to null; these may be intentionally left for further processing
        return new Skill(
                dbSkill.getId(),
                dbSkill.getTitle(),
                null,
                null
        );
    }
}
