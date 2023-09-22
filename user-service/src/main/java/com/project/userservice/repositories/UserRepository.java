package com.project.userservice.repositories;

import com.project.userservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
