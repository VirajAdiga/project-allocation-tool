package com.theja.projectallocationservice.Integration_tests.applications;

import com.theja.projectallocationservice.config.RestTemplateConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = RestTemplateConfig.class)
public class UpdateApplicationStatusIT {

    @Autowired
    private RestTemplate restTemplate;
    private Properties testTokens = new Properties();

    @BeforeEach
    public void setUp() {
        try {
            // Load the properties file
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application-test.properties");
            testTokens.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testEmployeeAcceptApplicationShouldThrowUnauthorizedException() {
        String token = testTokens.getProperty("employee_token");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-Type", "application/json");

        String applicationJsonBody = "{}"; // Provide valid application JSON body here
        HttpEntity<?> entity = new HttpEntity<>(applicationJsonBody, headers);

        try {
            restTemplate.exchange(
                    "http://localhost:9091/api/v1/applications/1/status?newStatus=ALLOCATED",
                    HttpMethod.PATCH,
                    entity,
                    String.class
            );
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.UNAUTHORIZED, e.getStatusCode());
        }
    }

    // TODO: setup teardown to reverse the action
    @Disabled("the testcase is under development")
    @Test
    public void testRecruiterAcceptApplicationShouldPass() {
        String token = testTokens.getProperty("recruiter_token");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-Type", "application/json");

        String applicationJsonBody = "{}"; // Provide valid application JSON body here
        HttpEntity<?> entity = new HttpEntity<>(applicationJsonBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:9091/api/v1/applications/43/status?newStatus=ALLOCATED",
                HttpMethod.PATCH,
                entity,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateApplicationStatusWhichIsNotFound() {
        String token = testTokens.getProperty("recruiter_token");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-Type", "application/json");

        String applicationJsonBody = "{}"; // Provide valid application JSON body here
        HttpEntity<?> entity = new HttpEntity<>(applicationJsonBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    "http://localhost:9091/api/v1/applications/1/status?newStatus=ALLOCATED",
                    HttpMethod.PATCH,
                    entity,
                    String.class
            );
        }
        catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }

    }

}
