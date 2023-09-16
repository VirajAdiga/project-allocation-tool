package com.project.userservice.mappers;

import com.project.userservice.entities.DBUser;
import com.project.userservice.dto.PublicUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Component class responsible for mapping between DBUser entities and PublicUser models.
 */
@Component
public class UserMapper {
    @Autowired
    private SkillMapper skillMapper;  // Inject SkillMapper for skill mapping

    /**
     * Converts a list of DBUser entities to a list of PublicUser models.
     *
     * @param dbUsers The list of DBUser entities to convert.
     * @return A list of PublicUser models.
     */
    public List<PublicUser> entityToPublicModel(List<DBUser> dbUsers) {
        if (dbUsers == null) {
            return new ArrayList<>(); // Return an empty list if input is null.
        }
        // Use Java streams to map each DBUser entity to a PublicUser model.
        return dbUsers.stream().map(this::entityToPublicModel).collect(Collectors.toList());
    }

    /**
     * Converts a single DBUser entity to a PublicUser model.
     *
     * @param dbUser The DBUser entity to convert.
     * @return The corresponding PublicUser model.
     */
    public PublicUser entityToPublicModel(DBUser dbUser) {
        if (dbUser != null) {
            // Create a new PublicUser model using data from the DBUser entity.
            // Also map the user's skills using the injected SkillMapper.
            return new PublicUser(dbUser.getId(), dbUser.getName(), dbUser.getEmail(), dbUser.getRole(), dbUser.isInterviewer(), skillMapper.entityToModel(dbUser.getSkills()));
        } else {
            return null; // Return null if input entity is null.
        }
    }
}
