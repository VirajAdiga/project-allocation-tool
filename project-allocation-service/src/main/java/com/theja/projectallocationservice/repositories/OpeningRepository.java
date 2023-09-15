package com.theja.projectallocationservice.repositories;

import com.theja.projectallocationservice.models.DBOpening;
import com.theja.projectallocationservice.models.DBSkill;
import com.theja.projectallocationservice.models.DBUser;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing database operations related to openings.
 */
@Repository
public interface OpeningRepository extends JpaRepository<DBOpening, Long> {

    /**
     * Retrieves a list of openings associated with a specific project.
     *
     * @param projectId The ID of the project.
     * @return A list of openings associated with the project.
     */
    List<DBOpening> findByProjectId(Long projectId);

    /**
     * Retrieves a pageable list of openings based on appliedBy and postedBy conditions.
     *
     * @param appliedBy      Specifies if openings are filtered by those applied by the logged-in user.
     * @param postedBy       Specifies if openings are filtered by those posted by the logged-in user.
     * @param pageable       Pageable object to control pagination and sorting.
     * @param loggedinUserId ID of the logged-in user.
     * @return A Page containing filtered openings.
     */
    @Query(value = "SELECT DISTINCT o.* FROM openings o LEFT JOIN applications a ON o.id = a.opening_id\n" +
            "    WHERE ((:appliedBy IS null) OR (:appliedBy IS TRUE AND a.candidate_id = :loggedinUserId) OR (:appliedBy IS FALSE AND (a.candidate_id IS null\n" +
            "            OR o.id not in (SELECT op.id FROM openings op JOIN applications ap ON ap.opening_id = op.id WHERE ap.candidate_id = :loggedinUserId))))\n" +
            "    AND ((:postedBy IS null) OR (:postedBy IS TRUE AND o.created_by = :loggedinUserId) OR (:postedBy IS FALSE AND o.created_by != :loggedinUserId))",
            nativeQuery = true, countProjection = "*")
    Page<DBOpening> fetchOpenings(Boolean appliedBy, Boolean postedBy, Pageable pageable, Long loggedinUserId);

    /**
     * Checks if an opening with the same combination of attributes exists for the specified project.
     *
     * @param title     The title of the opening.
     * @param details   The details of the opening.
     * @param level     The experience level of the opening.
     * @param location  The work location of the opening.
     * @param projectId The ID of the project associated with the opening.
     * @return {@code true} if an opening with the same attributes exists, {@code false} otherwise.
     */
    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM DBOpening o " +
            "WHERE o.title = :title " +
            "AND o.details = :details " +
            "AND o.level = :level " +
            "AND o.location = :location " +
            "AND o.project.id = :projectId")
    boolean existsByAttributesAndProjectId(
            @Param("title") String title,
            @Param("details") String details,
            @Param("level") Integer level,
            @Param("location") String location,
            @Param("projectId") Long projectId
    );
}
