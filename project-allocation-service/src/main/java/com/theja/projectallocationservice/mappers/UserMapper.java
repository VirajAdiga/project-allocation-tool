package com.theja.projectallocationservice.mappers;

import com.theja.projectallocationservice.models.DBUser;
import com.theja.projectallocationservice.models.PublicUser;
import com.theja.projectallocationservice.models.User;
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
    public Collection<User> entityToModel(Collection<DBUser> dbUsers) {
        if (dbUsers == null) {
            return new ArrayList<>();
        }
        // Map each DBUser entity to its corresponding User model object using Java Stream API
        return dbUsers.stream().map(this::entityToModel).collect(Collectors.toList());
    }

    // Convert a DBUser entity to a User model object
    public User entityToModel(DBUser dbUser) {
        return new User(
                dbUser.getId(),
                dbUser.getName(),
                dbUser.getPassword(),
                dbUser.getEmail(),
                skillMapper.entityToModel(dbUser.getSkills()),
                null,
                null,
                null,
                null
        );
    }

    // Convert a DBUser entity to a PublicUser model object
    public PublicUser entityToPublicModel(DBUser dbUser) {
        if (dbUser != null) {
            return new PublicUser(
                    dbUser.getId(),
                    dbUser.getName(),
                    dbUser.getEmail(),
                    skillMapper.entityToModel(dbUser.getSkills())
            );
        } else {
            return null;
        }
    }

    // Convert a list of DBUser entities to a list of PublicUser model objects
    public List<PublicUser> entityToPublicModel(List<DBUser> dbUsers) {
        // Map each DBUser entity to its corresponding PublicUser model object using Java Stream API
        return dbUsers.stream().map(this::entityToPublicModel).collect(Collectors.toList());
    }
}
