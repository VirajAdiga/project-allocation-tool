package com.theja.projectallocationservice.controllers;

import com.theja.projectallocationservice.mappers.SkillMapper;
import com.theja.projectallocationservice.entities.*;
import com.theja.projectallocationservice.services.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * The `SkillController` class manages HTTP requests related to skills.
 * It provides endpoints for retrieving and adding skills.
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Skills", description = "Endpoints related to managing skills")
public class SkillController {
    // Autowired fields for services and mappers...
    @Autowired
    private SkillService skillService;
    @Autowired
    private SkillMapper skillMapper;

    /**
     * Retrieves a list of all available skills.
     *
     * @return The response containing a list of skill models.
     */
    // Get all skills
    @GetMapping("/skills")
    @Operation(summary = "Get all skills", description = "Retrieve a list of all available skills")
    @ApiResponse(responseCode = "200", description = "List of skills retrieved successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = com.theja.projectallocationservice.dto.Skill.class))))
    public ResponseEntity<List<com.theja.projectallocationservice.dto.Skill>> getAllSkills() {
        // Fetch all skills from the database and return them in the response
        List<Skill> skills = skillService.getAllSkills();
        return ResponseEntity.ok(skillMapper.entityToModel(skills));
    }

    /**
     * Adds a new skill to the system.
     *
     * @param skill The skill entity to be added.
     * @return The response containing the created skill model.
     */
    // Add a new skill
    @PostMapping("/skills")
    @Operation(summary = "Add a new skill", description = "Add a new skill to the system")
    @ApiResponse(responseCode = "200", description = "Skill added successfully", content = @Content(schema = @Schema(implementation = com.theja.projectallocationservice.dto.Skill.class)))
    public ResponseEntity<com.theja.projectallocationservice.dto.Skill> addSkill(@RequestBody Skill skill) {
        // Create a new skill in the database and return the created skill in the response
        Skill createdSkill = skillService.createSkill(skill);
        return ResponseEntity.ok(skillMapper.entityToModel(createdSkill));
    }
}