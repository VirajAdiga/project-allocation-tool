package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.dto.EmailMessage;
import com.theja.projectallocationservice.dto.OpeningSearchMessage;
import com.theja.projectallocationservice.dto.PublicUser;
import com.theja.projectallocationservice.entities.Opening;
import com.theja.projectallocationservice.dto.RequestContext;
import com.theja.projectallocationservice.entities.Skill;
import com.theja.projectallocationservice.entities.enums.EmailTriggerActions;
import com.theja.projectallocationservice.exceptions.*;
import com.theja.projectallocationservice.mappers.EmailTriggerToMessageMapper;
import com.theja.projectallocationservice.repositories.OpeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing opening-related operations.
 */
@Service
public class OpeningService {
    @Autowired
    private OpeningRepository openingRepository;
    @Autowired
    private RequestContext requestContext;
    @Autowired
    private RabbitmqMessageService rabbitmqMessageService;
    @Autowired
    private EmailTriggerToMessageMapper emailTriggerToMessageMapper;

    /**
     * Retrieves a page of openings based on specified parameters.
     *
     * @param pageSize      The number of openings per page.
     * @param pageNumber    The page number.
     * @param appliedBySelf True if the openings are applied by the logged-in user, false otherwise.
     * @param postedBySelf  True if the openings are posted by the logged-in user, false otherwise.
     * @return A page of openings.
     */
    public Page<Opening> getAllOpenings(Integer pageSize, Integer pageNumber, Boolean appliedBySelf, Boolean postedBySelf) {
        if (pageSize == null) pageSize = 1000;
        if (pageNumber == null) pageNumber = 0;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        try {
            return openingRepository.fetchOpenings(appliedBySelf, postedBySelf, pageRequest, requestContext.getLoggedinUser().getId());
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }

    /**
     * Retrieves all openings for a specific project.
     *
     * @param projectId The ID of the project.
     * @return A list of openings for the project.
     */
    public List<Opening> getAllOpeningsForProject(Long projectId) {
        try {
            return openingRepository.findByProjectId(projectId);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }

    /**
     * Retrieves an opening by its ID.
     *
     * @param id The ID of the opening to retrieve.
     * @return The opening with the specified ID, or null if not found.
     */
    public Opening getOpeningById(Long id) {
        Optional<Opening> opening;
        try {
            opening = openingRepository.findById(id);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
        if (opening.isEmpty()){
            throw new ResourceNotFoundException("Opening not found with id " + id);
        }
        return opening.get();
    }

    /**
     * Creates a new opening.
     *
     * @param opening The opening to create.
     * @return The created opening.
     */
    public Opening createOpening(Opening opening, PublicUser user) {
        try {
            Opening openingSaved = openingRepository.save(opening);
            EmailMessage emailMessage = EmailMessage.builder().
                    recipient(user.getEmail()).
                    subject("Opening created").
                    body(emailTriggerToMessageMapper.getEmailCode(EmailTriggerActions.OPENING_CREATION)).
                    build();
            rabbitmqMessageService.sendMessageToQueue(emailMessage);
            OpeningSearchMessage openingSearchMessage = OpeningSearchMessage.builder().
                    id(openingSaved.getId()).
                    title(openingSaved.getTitle()).
                    details(openingSaved.getDetails()).
                    level(openingSaved.getLevel()).
                    location(openingSaved.getLocation()).
                    status(openingSaved.getStatus().toString()).
                    projectName(openingSaved.getProject().getTitle()).
                    skills(openingSaved.getSkills().stream().map(Skill::getTitle).collect(Collectors.toList())).
                    actionType("CREATE").
                    build();
            rabbitmqMessageService.sendMessageToQueue(openingSearchMessage);
            return openingSaved;
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }

    /**
     * Checks if a duplicate opening with the same combination of attributes already exists for the specified project.
     *
     * @param opening The opening to check for duplication.
     * @param projectId The ID of the project associated with the opening.
     * @return {@code true} if a duplicate opening exists, {@code false} otherwise.
     */
    public boolean isDuplicateOpening(Opening opening, Long projectId) {
        boolean openingExists;
        try {
            openingExists = openingRepository.existsByAttributesAndProjectId(
                    opening.getTitle(),
                    opening.getDetails(),
                    opening.getLevel(),
                    opening.getLocation(),
                    projectId
            );
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
        if (openingExists){
            throw new OpeningAlreadyExistsException("Opening already exists with project id " + projectId);
        }
        return false;
    }

    /**
     * Updates an existing opening.
     *
     * @param id      The ID of the opening to update.
     * @param opening The opening data to update.
     * @return The updated opening, or null if the opening is not found.
     */
    public Opening updateOpening(Long id, Opening opening) {
        Optional<Opening> existingOpening;
        try {
            existingOpening = openingRepository.findById(id);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
        if (existingOpening.isPresent()) {
            opening.setId(id);
            return openingRepository.save(opening);
        } else {
            throw new OpeningNotFoundException(id);
        }
    }

    /**
     * Deletes an opening by its ID.
     *
     * @param id The ID of the opening to delete.
     * @return True if the opening is deleted, false if the opening is not found.
     */
    public boolean deleteOpening(Long id) {
        Optional<Opening> opening;
        try {
            opening = openingRepository.findById(id);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
        if (opening.isPresent()) {
            openingRepository.deleteById(id);
            OpeningSearchMessage openingSearchMessage = OpeningSearchMessage.builder().
                    id(opening.get().getId()).
                    actionType("DELETE").
                    build();
            rabbitmqMessageService.sendMessageToQueue(openingSearchMessage);
            return true;
        } else {
            throw new OpeningNotFoundException(id);
        }
    }
}
