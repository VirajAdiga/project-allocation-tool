package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.dto.CreateProjectDTO;
import com.theja.projectallocationservice.entities.Project;
import com.theja.projectallocationservice.exceptions.DatabaseAccessException;
import com.theja.projectallocationservice.exceptions.ResourceNotFoundException;
import com.theja.projectallocationservice.exceptions.ServerSideGeneralException;
import com.theja.projectallocationservice.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
    public Page<Project> getAllProjects(Integer pageSize, Integer pageNumber) {
        if (pageSize == null) pageSize = 1000;
        if (pageNumber == null) pageNumber = 0;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        try {
            return projectRepository.findAll(pageRequest);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }

    /**
     * Retrieves a project by its unique identifier.
     *
     * @param id The ID of the project to retrieve.
     * @return The retrieved project, or null if not found.
     */
    public Project getProjectById(Long id) {
        Optional<Project> project;
        try {
            project = projectRepository.findById(id);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
        if (project.isEmpty()){
            throw new ResourceNotFoundException("Project not found with id " + id);
        }
        return project.get();
    }

    /**
     * Retrieves projects associated with the specified user.
     *
     * @param userId The ID of the user for whom to retrieve associated projects.
     * @return A list of projects associated with the user.
     */
    public List<Project> getProjectsForUser(Long userId) {
        // Implement the logic to fetch projects associated with the user
        try {
            return projectRepository.getProjectsForUser(userId);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }

    /**
     * Creates a new project based on the provided project data.
     *
     * @param projectDTO The data for creating the new project.
     * @return The created project.
     */
    public Project createProject(CreateProjectDTO projectDTO) {
        Project project = new Project();
        project.setTitle(projectDTO.getTitle());
        project.setDetails(projectDTO.getDetails());
        try {
            return projectRepository.save(project);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }

    /**
     * Updates an existing project with the provided updated project data.
     *
     * @param id The ID of the project to be updated.
     * @param updatedProjectData The updated data for the project.
     * @return The updated project, or null if the project with the given ID is not found.
     */
    public Project updateProject(Long id, Project updatedProjectData) {
        Optional<Project> existingProjectOpt;
        try {
            existingProjectOpt = projectRepository.findById(id);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
        if (existingProjectOpt.isEmpty()) {
            throw new ResourceNotFoundException("Project not found with id " + id);
        }
        Project existingProject = existingProjectOpt.get();
        // Update the basic properties of the project
        existingProject.setTitle(updatedProjectData.getTitle());
        existingProject.setDetails(updatedProjectData.getDetails());

        // Check if the updatedProjectData has openings and update the existing openings
        if (updatedProjectData.getOpenings() != null) {
            // Clear the existing openings and add the new ones
            existingProject.getOpenings().clear();
            existingProject.getOpenings().addAll(updatedProjectData.getOpenings());
        }
        try {
            // Save the updated project
            return projectRepository.save(existingProject);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }

    /**
     * Deletes a project with the specified ID.
     *
     * @param id The ID of the project to be deleted.
     * @return True if the project was deleted, false if the project with the given ID was not found.
     */
    public boolean deleteProject(Long id) {
        Optional<Project> project;
        // Find the project using the provided project ID
        try {
            project = projectRepository.findById(id);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
        // Check if the project with the given ID exists
        if (project.isPresent()) {
            // Delete the project from the repository
            projectRepository.deleteById(id);
            return true; // Indicate that the project was deleted
        } else {
            throw new ResourceNotFoundException("Project not found with id " + id); // Indicate that the project with the given ID was not found
        }
    }

    /**
     * Allocates a user to the specified project.
     *
     * @param project The project to which the user will be allocated.
     * @param candidateId The user to be allocated to the project.
     */
    public void allocateUser(Project project, Long candidateId) {
        // Check if the allocatedUsers set of the project is not initialized
        if (project.getAllocatedUsersId() == null) {
            // Initialize the allocatedUsers set with an empty HashSet
            project.setAllocatedUsersId(new HashSet<>());
        }

        // Add the candidate user to the allocatedUsers set
        project.getAllocatedUsersId().add(candidateId);

        try {
            // Save the updated project with the allocated user to the repository
            projectRepository.save(project);
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }


    /**
     * Retrieves a list of all projects without applying pagination.
     *
     * @return A list containing all projects from the repository.
     */
    public List<Project> getAllProjectsWithoutPagination() {
        try {
            // Retrieve and return a list of all projects from the repository
            return projectRepository.findAll();
        }
        catch (DataAccessException exception){
            throw new DatabaseAccessException("Error accessing the database");
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }
}
