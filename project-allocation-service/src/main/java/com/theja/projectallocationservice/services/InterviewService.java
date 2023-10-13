package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.exceptions.DatabaseAccessException;
import com.theja.projectallocationservice.exceptions.InterviewNotFoundException;
import com.theja.projectallocationservice.exceptions.ResourceNotFoundException;
import com.theja.projectallocationservice.entities.Interview;
import com.theja.projectallocationservice.exceptions.ServerSideGeneralException;
import com.theja.projectallocationservice.repositories.InterviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing interview-related operations.
 */
@Service
public class InterviewService {
    @Autowired
    private InterviewRepository interviewRepository;

    /**
     * Retrieves an interview by its ID.
     *
     * @param id The ID of the interview to retrieve.
     * @return The interview with the specified ID.
     * @throws ResourceNotFoundException If the interview is not found.
     */
    public Interview getInterviewById(Long id) {
        Optional<Interview> interview;
        try {
            interview = interviewRepository.findById(id);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
        if (interview.isEmpty()){
            throw new ResourceNotFoundException("Interview not found with id " + id);
        }
        return interview.get();
    }

    /**
     * Retrieves all interviews associated with a particular interviewer ID.
     *
     * @param interviewerId The ID of the interviewer for whom to retrieve interviews.
     * @return A list of DBInterview entities associated with the provided interviewer ID.
     */
    public List<Interview> getInterviewsByInterviewerId(Long interviewerId) {
        try {
            return interviewRepository.findByInterviewerId(interviewerId);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }

    public Interview updateInterviewFeedback(Long interviewId, String feedback) {
        Optional<Interview> optionalInterview;
        try {
            optionalInterview = interviewRepository.findById(interviewId);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
        if (optionalInterview.isPresent()) {
            Interview interview = optionalInterview.get();
            interview.setFeedback(feedback);
            return interviewRepository.save(interview);
        } else {
            throw new ResourceNotFoundException("Interview not found with ID: " + interviewId);
        }
    }

    /**
     * Retrieves a list of interviews associated with a specific application ID.
     *
     * @param applicationId The unique identifier of the application
     * @return A list of interviews associated with the application
     * @throws InterviewNotFoundException If no interviews are found for the given application ID
     */
    public List<Interview> getInterviewsByApplicationId(Long applicationId) {
        try {
            return interviewRepository.findByApplicationId(applicationId);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }

    /**
     * Creates a new interview.
     *
     * @param interview The interview to create.
     * @return The created interview.
     */
    public Interview createInterview(Interview interview) {
        try{
            return interviewRepository.save(interview);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }

    /**
     * Updates an existing interview.
     *
     * @param id       The ID of the interview to update.
     * @param interview The interview data to update.
     * @return The updated interview.
     * @throws ResourceNotFoundException If the interview is not found.
     */
    public Interview updateInterview(Long id, Interview interview) {
        try {
            Interview existingInterview = getInterviewById(id);
            // Update the properties of existingInterview with the properties from the provided interview
            existingInterview.setStatus(interview.getStatus());
            existingInterview.setScheduledTime(interview.getScheduledTime());
            existingInterview.setApplication(interview.getApplication());
            return interviewRepository.save(existingInterview);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }

    /**
     * Deletes an interview by its ID.
     *
     * @param id The ID of the interview to delete.
     * @throws ResourceNotFoundException If the interview is not found.
     */
    public void deleteInterview(Long id) {
        if (!interviewRepository.existsById(id)){
            throw new ResourceNotFoundException("Interview not found with id " + id);
        }
        try {
            interviewRepository.deleteById(id);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }
}
