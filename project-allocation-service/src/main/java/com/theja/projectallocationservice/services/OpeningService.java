package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.entities.Opening;
import com.theja.projectallocationservice.dto.RequestContext;
import com.theja.projectallocationservice.repositories.OpeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing opening-related operations.
 */
@Service
public class OpeningService {
    @Autowired
    private OpeningRepository openingRepository;
    @Autowired
    private RequestContext requestContext;

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
        return openingRepository.fetchOpenings(appliedBySelf, postedBySelf, pageRequest, requestContext.getLoggedinUser().getId());
    }

    /**
     * Retrieves all openings for a specific project.
     *
     * @param projectId The ID of the project.
     * @return A list of openings for the project.
     */
    public List<Opening> getAllOpeningsForProject(Long projectId) {
        return openingRepository.findByProjectId(projectId);
    }

    /**
     * Retrieves an opening by its ID.
     *
     * @param id The ID of the opening to retrieve.
     * @return The opening with the specified ID, or null if not found.
     */
    public Opening getOpeningById(Long id) {
        Optional<Opening> opening = openingRepository.findById(id);
        return opening.orElse(null);
    }

    /**
     * Creates a new opening.
     *
     * @param opening The opening to create.
     * @return The created opening.
     */
    public Opening createOpening(Opening opening) {
        return openingRepository.save(opening);
    }

    /**
     * Checks if a duplicate opening with the same combination of attributes already exists for the specified project.
     *
     * @param opening The opening to check for duplication.
     * @param projectId The ID of the project associated with the opening.
     * @return {@code true} if a duplicate opening exists, {@code false} otherwise.
     */
    public boolean isDuplicateOpening(Opening opening, Long projectId) {
        return openingRepository.existsByAttributesAndProjectId(
                opening.getTitle(),
                opening.getDetails(),
                opening.getLevel(),
                opening.getLocation(),
                projectId
        );
    }

    /**
     * Updates an existing opening.
     *
     * @param id      The ID of the opening to update.
     * @param opening The opening data to update.
     * @return The updated opening, or null if the opening is not found.
     */
    public Opening updateOpening(Long id, Opening opening) {
        Optional<Opening> existingOpening = openingRepository.findById(id);
        if (existingOpening.isPresent()) {
            opening.setId(id);
            return openingRepository.save(opening);
        } else {
            return null;
        }
    }

    /**
     * Deletes an opening by its ID.
     *
     * @param id The ID of the opening to delete.
     * @return True if the opening is deleted, false if the opening is not found.
     */
    public boolean deleteOpening(Long id) {
        Optional<Opening> opening = openingRepository.findById(id);
        if (opening.isPresent()) {
            openingRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}

