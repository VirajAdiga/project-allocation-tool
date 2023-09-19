package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.entities.enums.ApplicationStatus;
import com.theja.projectallocationservice.exceptions.ResourceNotFoundException;
import com.theja.projectallocationservice.entities.*;
import com.theja.projectallocationservice.repositories.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service class for managing application-related operations.
 */
@Service
public class ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;

    /**
     * Retrieves a page of applications based on the provided status.
     *
     * @param status   The status of applications to retrieve.
     * @param pageable Pageable object for pagination.
     * @return A page of applications with the specified status.
     */
    public Page<Application> getAllApplicationsByStatus(ApplicationStatus status, Pageable pageable) {
        if (status == null) {
            return applicationRepository.findAll(pageable);
        } else {
            return applicationRepository.findByStatus(status, pageable);
        }
    }

    /**
     * Retrieves an application by its ID.
     *
     * @param id The ID of the application to retrieve.
     * @return The application with the specified ID.
     * @throws ResourceNotFoundException If the application with the given ID is not found.
     */
    public Application getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
    }

    /**
     * Creates a new application.
     *
     * @param application The application to create.
     * @return The created application.
     */
    public Application createApplication(Application application) {
        return applicationRepository.save(application);
    }

    /**
     * Updates an existing application.
     *
     * @param id          The ID of the application to update.
     * @param application The updated application data.
     * @return The updated application.
     * @throws ResourceNotFoundException If the application with the given ID is not found.
     */
    public Application updateApplication(Long id, Application application) {
        Application existingApplication = getApplicationById(id);
        // Update the properties of existingApplication with the properties from the provided application
        existingApplication.setStatus(application.getStatus());
        existingApplication.setAppliedAt(application.getAppliedAt());
        existingApplication.setOpening(application.getOpening());
        return applicationRepository.save(existingApplication);
    }

    /**
     * Deletes an application by its ID.
     *
     * @param id The ID of the application to delete.
     * @throws ResourceNotFoundException If the application with the given ID is not found.
     */
    public void deleteApplication(Long id) {
        if (applicationRepository.existsById(id)) {
            applicationRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Application not found");
        }
    }

    /**
     * Retrieves an application by the opening ID and candidate ID.
     *
     * @param openingId   The ID of the opening.
     * @param candidateId The ID of the candidate.
     * @return The application associated with the specified opening and candidate.
     */
    public Application getApplicationByOpeningIdAndCandidateId(Long openingId, Long candidateId) {
        return applicationRepository.findByOpeningIdAndCandidateId(openingId, candidateId);
    }
}

