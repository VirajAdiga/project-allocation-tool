package com.theja.projectallocationservice.repositories;

import com.theja.projectallocationservice.models.DBInterview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing database operations related to interviews.
 */
@Repository
public interface InterviewRepository extends JpaRepository<DBInterview, Long> {
    // Custom query methods or additional repository operations can be defined here if needed

    List<DBInterview> findByInterviewerId(Long interviewerId);

    List<DBInterview> findByApplicationId(Long applicationId);
}
