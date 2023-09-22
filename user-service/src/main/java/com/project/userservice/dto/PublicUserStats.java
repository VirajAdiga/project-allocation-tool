package com.project.userservice.dto;


import com.project.userservice.entities.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicUserStats {
    private Integer id;
    private String name;
    private String email;
    private Role role;
    private boolean isInterviewer;
    private List<Skill> skills;
    private List<Long> interviews;
    private List<Long> applications;
    private List<Long> actions;
    private List<Long> openings;
}
