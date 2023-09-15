package com.project.userservice.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicUser {
    private Integer id;
    private String name;
    private String email;
    private Role role;
    private boolean isInterviewer;
    private List<Skill> skills;
}
