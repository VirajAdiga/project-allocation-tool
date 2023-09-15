package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.models.CreateProjectDTO;
import com.theja.projectallocationservice.models.DBProject;
import com.theja.projectallocationservice.models.DBUser;
import com.theja.projectallocationservice.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;


/**
 * Service class for managing project-related operations.
 */
@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    /**
     * Retrieves a page of projects based on specified parameters.
     *
     * @param pageSize   The number of projects per page.
     * @param pageNumber The page number.
     * @return A page of projects.
     */
    public Page<DBProject> getAllProjects(Integer pageSize, Integer pageNumber) {
        if (pageSize == null) pageSize = 1000;
        if (pageNumber == null) pageNumber = 0;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return projectRepository.findAll(pageRequest);
    }

    /**
     * Retrieves a project by its unique identifier.
     *
     * @param id The ID of the project to retrieve.
     * @return The retrieved project, or null if not found.
     */
    public DBProject getProjectById(Long id) {
        Optional<DBProject> project = projectRepository.findById(id);
        return project.orElse(null);
    }

    /**
     * Retrieves projects associated with the specified user.
     *
     * @param userId The ID of the user for whom to retrieve associated projects.
     * @return A list of projects associated with the user.
     */
    public List<DBProject> getProjectsForUser(Long userId) {
        // Implement the logic to fetch projects associated with the user
        return projectRepository.getProjectsForUser(userId);
    }

    /**
     * Creates a new project based on the provided project data.
     *
     * @param projectDTO The data for creating the new project.
     * @return The created project.
     */
    public DBProject createProject(CreateProjectDTO projectDTO) {
        DBProject project = new DBProject();
        project.setTitle(projectDTO.getTitle());
        project.setDetails(projectDTO.getDetails());
        return projectRepository.save(project);
    }

    /**
     * Updates an existing project with the provided updated project data.
     *
     * @param id The ID of the project to be updated.
     * @param updatedProjectData The updated data for the project.
     * @return The updated project, or null if the project with the given ID is not found.
     */
    public DBProject updateProject(Long id, DBProject updatedProjectData) {
        Optional<DBProject> existingProjectOpt = projectRepository.findById(id);
        if (existingProjectOpt.isPresent()) {
            DBProject existingProject = existingProjectOpt.get();
            // Update the basic properties of the project
            existingProject.setTitle(updatedProjectData.getTitle());
            existingProject.setDetails(updatedProjectData.getDetails());

            // Check if the updatedProjectData has openings and update the existing openings
            if (updatedProjectData.getOpenings() != null) {
                // Clear the existing openings and add the new ones
                existingProject.getOpenings().clear();
                existingProject.getOpenings().addAll(updatedProjectData.getOpenings());
            }

            // Save the updated project
            return projectRepository.save(existingProject);
        } else {
            return null;
        }
    }

    /**
     * Deletes a project with the specified ID.
     *
     * @param id The ID of the project to be deleted.
     * @return True if the project was deleted, false if the project with the given ID was not found.
     */
    public boolean deleteProject(Long id) {
        // Find the project using the provided project ID
        Optional<DBProject> project = projectRepository.findById(id);

        // Check if the project with the given ID exists
        if (project.isPresent()) {
            // Delete the project from the repository
            projectRepository.deleteById(id);
            return true; // Indicate that the project was deleted
        } else {
            return false; // Indicate that the project with the given ID was not found
        }
    }

    /**
     * Allocates a user to the specified project.
     *
     * @param project The project to which the user will be allocated.
     * @param candidate The user to be allocated to the project.
     */
    public void allocateUser(DBProject project, DBUser candidate) {
        // Check if the allocatedUsers set of the project is not initialized
        if (project.getAllocatedUsers() == null) {
            // Initialize the allocatedUsers set with an empty HashSet
            project.setAllocatedUsers(new HashSet<>());
        }

        // Add the candidate user to the allocatedUsers set
        project.getAllocatedUsers().add(candidate);

        // Save the updated project with the allocated user to the repository
        projectRepository.save(project);
    }


    /**
     * Retrieves a list of all projects without applying pagination.
     *
     * @return A list containing all projects from the repository.
     */
    public List<DBProject> getAllProjectsWithoutPagination() {
        // Retrieve and return a list of all projects from the repository
        return projectRepository.findAll();
    }

}

