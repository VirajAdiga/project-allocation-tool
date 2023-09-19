package com.project.userservice.mappers;

import com.project.userservice.entities.User;
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
     * @param users The list of DBUser entities to convert.
     * @return A list of PublicUser models.
     */
    public List<PublicUser> entityToPublicModel(List<User> users) {
        if (users == null) {
            return new ArrayList<>(); // Return an empty list if input is null.
        }
        // Use Java streams to map each DBUser entity to a PublicUser model.
        return users.stream().map(this::entityToPublicModel).collect(Collectors.toList());
    }

    /**
     * Converts a single DBUser entity to a PublicUser model.
     *
     * @param user The DBUser entity to convert.
     * @return The corresponding PublicUser model.
     */
    public PublicUser entityToPublicModel(User user) {
        if (user != null) {
            // Create a new PublicUser model using data from the DBUser entity.
            // Also map the user's skills using the injected SkillMapper.
            return new PublicUser(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.isInterviewer(), skillMapper.entityToModel(user.getSkills()));
        } else {
            return null; // Return null if input entity is null.
        }
    }
}
