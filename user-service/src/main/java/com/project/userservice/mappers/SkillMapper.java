package com.project.userservice.mappers;

import com.project.userservice.entities.DBSkill;
import com.project.userservice.dto.Skill;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Component class responsible for mapping between DBSkill entities and Skill models.
 */
@Component
public class SkillMapper {

    /**
     * Converts a list of DBSkill entities to a list of Skill models.
     *
     * @param dbSkills The list of DBSkill entities to convert.
     * @return A list of Skill models.
     */
    public List<Skill> entityToModel(List<DBSkill> dbSkills) {
        if (dbSkills == null) {
            return new ArrayList<>(); // Return an empty list if input is null.
        }
        // Use Java streams to map each DBSkill entity to a Skill model.
        return dbSkills.stream().map(this::entityToModel).collect(Collectors.toList());
    }

    /**
     * Converts a single DBSkill entity to a Skill model.
     *
     * @param dbSkill The DBSkill entity to convert.
     * @return The corresponding Skill model.
     */
    public Skill entityToModel(DBSkill dbSkill) {
        // Create a new Skill model using data from the DBSkill entity.
        return new Skill(dbSkill.getId(), dbSkill.getTitle(), null);
    }
}