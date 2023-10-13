package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.entities.Skill;
import com.theja.projectallocationservice.exceptions.DatabaseAccessException;
import com.theja.projectallocationservice.exceptions.ServerSideGeneralException;
import com.theja.projectallocationservice.exceptions.SkillNotFoundException;
import com.theja.projectallocationservice.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkillService {
    @Autowired
    private SkillRepository skillRepository;

    /**
     * Retrieve a skill by its unique ID.
     *
     * @param skillId The ID of the skill to retrieve.
     * @return An Optional containing the retrieved skill, or an empty Optional if not found.
     */
    public Skill getSkillById(Long skillId) {
        Optional<Skill> skill;
        try {
            skill = skillRepository.findById(skillId);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
        if (skill.isEmpty()) {
            throw new SkillNotFoundException("Skill not found with id " + skillId);
        }
        return skill.get();
    }

    /**
     * Create a new skill.
     *
     * @param skill The skill to be created.
     * @return The created skill.
     */
    public Skill createSkill(Skill skill) {
        try {
            return skillRepository.save(skill);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }

    /**
     * Retrieve a list of all skills.
     *
     * @return A list containing all skills.
     */
    public List<Skill> getAllSkills() {
        try {
            return skillRepository.findAll();
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }
}
