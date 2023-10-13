package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.entities.enums.ApplicationStatus;
import com.theja.projectallocationservice.exceptions.DatabaseAccessException;
import com.theja.projectallocationservice.exceptions.ResourceNotFoundException;
import com.theja.projectallocationservice.entities.*;
import com.theja.projectallocationservice.exceptions.ServerSideGeneralException;
import com.theja.projectallocationservice.repositories.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        try {
            if (status == null) {
                return applicationRepository.findAll(pageable);
            } else {
                return applicationRepository.findByStatus(status, pageable);
            }
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
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
        Optional<Application> application;
        try {
            application = applicationRepository.findById(id);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
        if (application.isEmpty()){
            throw new ResourceNotFoundException("Application not found with id " + id);
        }
        return application.get();
    }

    /**
     * Creates a new application.
     *
     * @param application The application to create.
     * @return The created application.
     */
    public Application createApplication(Application application) {
        try {
            return applicationRepository.save(application);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
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
        try {
            return applicationRepository.save(existingApplication);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }

    /**
     * Deletes an application by its ID.
     *
     * @param id The ID of the application to delete.
     * @throws ResourceNotFoundException If the application with the given ID is not found.
     */
    public void deleteApplication(Long id) {
        if (!applicationRepository.existsById(id)){
            throw new ResourceNotFoundException("Application not found with id " + id);
        }
        try {
            applicationRepository.deleteById(id);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
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
        Application application;
        try {
            application = applicationRepository.findByOpeningIdAndCandidateId(openingId, candidateId);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
        if (application == null){
            throw new ResourceNotFoundException("Resource does not exist with opening id " + openingId + " and candidate id " + candidateId);
        }
        return application;
    }
}
