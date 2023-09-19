package com.theja.projectallocationservice.repositories;

import com.theja.projectallocationservice.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Repository interface for managing database operations related to users.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves a page of users who are not allocated to any projects.
     *
     * @param pageable Pageable object for pagination.
     * @return A page of users in the free pool.
     */
    @Query(value = "SELECT * FROM users WHERE id not in (SELECT user_id FROM users_projects);", nativeQuery = true)
    Page<User> getFreePoolUsers(Pageable pageable);

    /**
     * Retrieves a page of users who have been allocated to projects within a specified date range.
     *
     * @param startDate Start date of the allocation period.
     * @param endDate   End date of the allocation period.
     * @param pageable  Pageable object for pagination.
     * @return A page of users allocated within the specified date range.
     */
    @Query(value = "SELECT * FROM users WHERE id in (SELECT user_id FROM users_projects WHERE allocated_date between :startDate and :endDate);", nativeQuery = true)
    Page<User> getAllAllocatedUsers(Date startDate, Date endDate, Pageable pageable);

    // You can define additional custom query methods here if needed
}
