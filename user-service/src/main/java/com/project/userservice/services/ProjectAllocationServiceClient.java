package com.project.userservice.services;

import com.project.userservice.dto.Skill;

public interface ProjectAllocationServiceClient {

    /**
     * Get skill information.
     *
     * @param skillId Id of the skill.
     * @return Skill information.
     */
    Skill getSkill(Long skillId);
}
