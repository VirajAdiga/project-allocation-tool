package com.theja.projectallocationservice.mappers;

import com.theja.projectallocationservice.entities.User;
import com.theja.projectallocationservice.dto.PublicUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    @Autowired
    private SkillMapper skillMapper;

    // Convert a collection of DBUser entities to a collection of User model objects
    public Collection<com.theja.projectallocationservice.dto.User> entityToModel(Collection<User> users) {
        if (users == null) {
            return new ArrayList<>();
        }
        // Map each DBUser entity to its corresponding User model object using Java Stream API
        return users.stream().map(this::entityToModel).collect(Collectors.toList());
    }

    // Convert a DBUser entity to a User model object
    public com.theja.projectallocationservice.dto.User entityToModel(User user) {
        return new com.theja.projectallocationservice.dto.User(
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getEmail(),
                skillMapper.entityToModel(user.getSkills()),
                null,
                null,
                null,
                null
        );
    }

    // Convert a DBUser entity to a PublicUser model object
    public PublicUser entityToPublicModel(User user) {
        if (user != null) {
            return new PublicUser(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    skillMapper.entityToModel(user.getSkills())
            );
        } else {
            return null;
        }
    }

    // Convert a list of DBUser entities to a list of PublicUser model objects
    public List<PublicUser> entityToPublicModel(List<User> users) {
        // Map each DBUser entity to its corresponding PublicUser model object using Java Stream API
        return users.stream().map(this::entityToPublicModel).collect(Collectors.toList());
    }
}
