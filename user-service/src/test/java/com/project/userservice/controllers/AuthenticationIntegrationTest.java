package com.project.userservice.controllers;

import com.project.userservice.config.JwtAuthenticationFilter;
import com.project.userservice.entities.DBUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
public class AuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // You might need to inject repositories or services if required

    private String encodedPassword;

    @BeforeEach
    public void setup() {
        encodedPassword = passwordEncoder.encode("adminpassword");
        jwtAuthenticationFilter.setDisabled(true);
    }

    @Test
    public void testAuthentication() throws Exception {
        // Create a user in the database with the encoded password
        DBUser testUser = new DBUser();
        testUser.setEmail("admin@gmail.com");
        testUser.setPassword(encodedPassword);
        // Set other required fields

        // Save the testUser in your repository

        // Perform authentication request
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                .content("{\"email\": \"admin@gmail.com\", \"password\": \"adminpassword\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        // Assertions
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(jsonPath("$").isString());
        // You can add more assertions based on your needs
    }
}

