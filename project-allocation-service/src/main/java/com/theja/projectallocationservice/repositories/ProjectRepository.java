package com.theja.projectallocationservice.repositories;

import com.theja.projectallocationservice.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing database operations related to projects.
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    /**
     * Retrieves a list of projects associated with a specific user.
     *
     * @param userId ID of the user.
     * @return A list of projects associated with the user.
     */
    @Query(value = "SELECT p.* FROM projects p JOIN users_projects up ON p.id = up.project_id WHERE up.user_id = :userId", nativeQuery = true)
    List<Project> getProjectsForUser(Long userId);

    // You can define additional custom query methods here if needed
}
