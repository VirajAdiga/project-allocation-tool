package com.project.userservice.repositories;

import com.project.userservice.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing DBUser entities.
 */
@Repository
// Repository interface for managing DBUser entities.
public interface UserRepository extends JpaRepository<User, Long> {

   /**
    * Custom method to find a user by email.
    *
    * @param email The email of the user to find.
    * @return An optional containing the user if found.
    */
   Optional<User> findByEmail(Object email);

   List<User> findByIsInterviewerTrue(); // Custom query to retrieve users with is_interviewer = true

   /**
    * Retrieves a page of users who are not allocated to any projects.
    *
    * @param pageable Pageable object for pagination.
    * @return A page of users in the free pool.
    */
   @Query(value = "SELECT * FROM users WHERE role='EMPLOYEE' and projectAllocatedId IS NULL;", nativeQuery = true)
   Page<User> getFreePoolUsers(Pageable pageable);

   /**
    * Retrieves a page of users who have been allocated to projects within a specified date range.
    *
    * @param pageable  Pageable object for pagination.
    * @return A page of users allocated within the specified date range.
    */
   @Query(value = "SELECT * FROM users WHERE role='EMPLOYEE' and projectAllocatedId IS NOT NULL;", nativeQuery = true)
   Page<User> getAllAllocatedUsers(Pageable pageable);
}
