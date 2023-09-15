package com.theja.projectallocationservice.repositories;

import com.theja.projectallocationservice.models.DBSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing database operations related to skills.
 */
@Repository
public interface SkillRepository extends JpaRepository<DBSkill, Long> {

    /**
     * Retrieves a list of skills associated with a specific user.
     *
     * @param userId ID of the user.
     * @return A list of skills associated with the user.
     */
    List<DBSkill> findByUsersId(Long userId);

    /**
     * Retrieves a list of skills associated with a specific opening.
     *
     * @param openingId ID of the opening.
     * @return A list of skills associated with the opening.
     */
    List<DBSkill> findByOpeningsId(Long openingId);

    // You can define additional custom query methods here if needed
}
