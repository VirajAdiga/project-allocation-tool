package com.project.userservice.repositories;

import com.project.userservice.entities.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing DBSkill entities.
 */
@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
}
