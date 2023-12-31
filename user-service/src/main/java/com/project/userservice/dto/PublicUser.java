package com.project.userservice.dto;


import com.project.userservice.entities.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicUser {
    private Integer id;
    private String name;
    private String email;
    private Role role;
    private boolean isInterviewer;
}
