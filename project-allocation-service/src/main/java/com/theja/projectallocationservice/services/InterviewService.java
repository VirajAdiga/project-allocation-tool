package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.exceptions.InterviewNotFoundException;
import com.theja.projectallocationservice.exceptions.ResourceNotFoundException;
import com.theja.projectallocationservice.models.DBInterview;
import com.theja.projectallocationservice.repositories.InterviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public DBInterview getInterviewById(Long id) {
        return interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found"));
    }

    /**
     * Retrieves all interviews associated with a particular interviewer ID.
     *
     * @param interviewerId The ID of the interviewer for whom to retrieve interviews.
     * @return A list of DBInterview entities associated with the provided interviewer ID.
     */
    public List<DBInterview> getInterviewsByInterviewerId(Long interviewerId) {
        return interviewRepository.findByInterviewerId(interviewerId);
    }

    public DBInterview updateInterviewFeedback(Long interviewId, String feedback) {
        Optional<DBInterview> optionalInterview = interviewRepository.findById(interviewId);

        if (optionalInterview.isPresent()) {
            DBInterview interview = optionalInterview.get();
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
    public List<DBInterview> getInterviewsByApplicationId(Long applicationId) {
        List<DBInterview> interviews = interviewRepository.findByApplicationId(applicationId);

        if (interviews.isEmpty()) {
            throw new InterviewNotFoundException("No interviews found for application ID: " + applicationId);
        }

        return interviews;
    }

    /**
     * Creates a new interview.
     *
     * @param interview The interview to create.
     * @return The created interview.
     */
    public DBInterview createInterview(DBInterview interview) {
        return interviewRepository.save(interview);
    }

    /**
     * Updates an existing interview.
     *
     * @param id       The ID of the interview to update.
     * @param interview The interview data to update.
     * @return The updated interview.
     * @throws ResourceNotFoundException If the interview is not found.
     */
    public DBInterview updateInterview(Long id, DBInterview interview) {
        DBInterview existingInterview = getInterviewById(id);
        // Update the properties of existingInterview with the properties from the provided interview
        existingInterview.setStatus(interview.getStatus());
        existingInterview.setScheduledTime(interview.getScheduledTime());
        existingInterview.setApplication(interview.getApplication());
        return interviewRepository.save(existingInterview);
    }

    /**
     * Deletes an interview by its ID.
     *
     * @param id The ID of the interview to delete.
     * @throws ResourceNotFoundException If the interview is not found.
     */
    public void deleteInterview(Long id) {
        if (interviewRepository.existsById(id)) {
            interviewRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Interview not found");
        }
    }
}

