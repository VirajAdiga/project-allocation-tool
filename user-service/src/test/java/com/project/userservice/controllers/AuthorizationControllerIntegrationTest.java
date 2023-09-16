package com.project.userservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.userservice.services.AuthenticationService;
import com.project.userservice.services.AuthorizationService;
import com.project.userservice.config.JwtAuthenticationFilter;
import com.project.userservice.mappers.UserMapper;
import com.project.userservice.entities.DBUser;
import com.project.userservice.entities.enums.PermissionName;
import com.project.userservice.entities.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthorizationControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private AuthorizationService authorizationService;
    private String authToken; // Declare the authToken as a member variable
    private String encodedPassword;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    public void setUp() throws Exception {
        encodedPassword = passwordEncoder.encode("adminpassword");
        jwtAuthenticationFilter.setDisabled(true);

        // Create a DBUser instance to use as the principal
        DBUser dbUser = DBUser.builder()
                .id(1)
                .name("Admin")
                .email("admin@gmail.com")
                .role(Role.ADMIN) // Set the appropriate role
                .build();

        // Create an Authentication object with the DBUser as the principal
        Authentication authentication = new UsernamePasswordAuthenticationToken(dbUser, null);

        // Set the Authentication object in the SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate the token and store it
        String token = authorizationService.generateJWTTokenForUser(dbUser);
        authToken = token;
    }

    @Test
    public void testGetPermissionsForAdmin() throws Exception {
        // Perform the request and get the result actions
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/authorization/permissions")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON));

        // Define expected permissions for ADMIN role
        Set<PermissionName> expectedPermissions = new HashSet<>(Arrays.asList(
                PermissionName.CREATE_PROJECT,
                PermissionName.MANAGE_USERS,
                PermissionName.VIEW_USER_ACTIVITY,
                PermissionName.CREATE_OPENING,
                PermissionName.VIEW_PENDING_APPLICATIONS,
                PermissionName.VIEW_REPORTS,
                PermissionName.MANAGE_OPENINGS,
                PermissionName.REGISTER_USER,
                PermissionName.ADMIN_OWN_OPENINGS,
                PermissionName.ADMIN_OTHER_OPENINGS
                // Add other permissions here as needed
        ));

        // Convert the expected permissions to a JSON array
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedPermissionsJson = objectMapper.writeValueAsString(expectedPermissions);

        // Perform the assertion
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedPermissionsJson));
    }

    @Test
    public void testGetRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/authorization/role")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("\"ADMIN\""));
    }

    @Test
    public void testGetUser() throws Exception {
        // Perform the request and get the result actions
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/authorization/user")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Admin")) // Update with expected user's name
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("admin@gmail.com")); // Update with expected user's email
        // Add other assertions for the expected user data as needed
    }
}


