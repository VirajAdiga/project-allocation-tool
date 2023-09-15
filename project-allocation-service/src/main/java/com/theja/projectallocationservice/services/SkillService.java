package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.models.DBSkill;
import com.theja.projectallocationservice.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Optional<DBSkill> getSkillById(Long skillId) {
        return skillRepository.findById(skillId);
    }

    /**
     * Create a new skill.
     *
     * @param skill The skill to be created.
     * @return The created skill.
     */
    public DBSkill createSkill(DBSkill skill) {
        return skillRepository.save(skill);
    }

    /**
     * Retrieve a list of all skills.
     *
     * @return A list containing all skills.
     */
    public List<DBSkill> getAllSkills() {
        return skillRepository.findAll();
    }
}
