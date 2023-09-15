package com.theja.projectallocationservice.repositories;

import com.theja.projectallocationservice.models.ApplicationStatus;
import com.theja.projectallocationservice.models.DBApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing database operations related to applications.
 */
@Repository
public interface ApplicationRepository extends JpaRepository<DBApplication, Long> {

    /**
     * Find an application by the given opening ID and candidate ID.
     *
     * @param openingId   The ID of the opening associated with the application.
     * @param candidateId The ID of the candidate associated with the application.
     * @return The application matching the opening and candidate IDs, if found.
     */
    DBApplication findByOpeningIdAndCandidateId(Long openingId, Long candidateId);

    /**
     * Find applications by status using pagination.
     *
     * @param status   The status of applications to search for.
     * @param pageable Pageable object specifying the page number, size, and sorting.
     * @return A Page of applications with the specified status.
     */
    Page<DBApplication> findByStatus(ApplicationStatus status, Pageable pageable);

    // Custom query methods or additional repository operations can be defined here if needed
}
