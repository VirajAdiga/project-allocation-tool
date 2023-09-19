package com.theja.projectallocationservice.Integration_tests.openings;

import com.theja.projectallocationservice.config.RestTemplateConfig;
import com.theja.projectallocationservice.exceptions.CustomResponseErrorHandler;
import com.theja.projectallocationservice.dto.Opening;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.util.AssertionErrors.fail;

@SpringBootTest(classes = RestTemplateConfig.class)
public class CreateOpeningIT {
    private Properties testTokens = new Properties();

    @Autowired
    private RestTemplate restTemplate;

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

    @Disabled(value = "Test case needs teardown setup")
    @Test
    public void testCreateOpeningWorksFine() {
        String token = testTokens.getProperty("recruiter_token");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-Type", "application/json");

        // TODO: set this in Opening pojo and use jackson to convert to json string
        String openingJsonBody = "{\n" +
                "    \"title\": \"Java Senior Software Engineer\",\n" +
                "    \"details\": \"Building Enterprise backend\",\n" +
                "    \"level\": 1,\n" +
                "    \"location\": \"Chennai\",\n" +
                "    \"recruiter\": {\n" +
                "        \"id\": 20\n" +
                "    },\n" +
                "    \"skills\": [\n" +
                "        {\n" +
                "            \"id\": 1\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        HttpEntity<?> entity = new HttpEntity<>(openingJsonBody, headers);

        Opening createdOpening = restTemplate.postForObject(
                "http://localhost:9091/api/v1/projects/7/openings",
                entity,
                Opening.class
        );

        Assertions.assertNotNull(createdOpening.getId());
    }

    // TODO: teardown phase delete the data created above

    @Test
    public void testCreateDuplicateOpeningThrowsException() {
        String token = testTokens.getProperty("recruiter_token");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-Type", "application/json");

        // TODO: set this in Opening pojo and use jackson to convert to json string
        String openingJsonBody = "{\n" +
                "    \"title\": \"Java Senior Software Engineer\",\n" +
                "    \"details\": \"Building Enterprise backend\",\n" +
                "    \"level\": 1,\n" +
                "    \"location\": \"Chennai\",\n" +
                "    \"recruiter\": {\n" +
                "        \"id\": 20\n" +
                "    },\n" +
                "    \"skills\": [\n" +
                "        {\n" +
                "            \"id\": 1\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        HttpEntity<?> entity = new HttpEntity<>(openingJsonBody, headers);

        // Use assertThrows to check if the expected exception is thrown
        HttpClientErrorException exception = assertThrows(
                HttpClientErrorException.class,
                () -> restTemplate.postForObject(
                        "http://localhost:9091/api/v1/projects/7/openings",
                        entity,
                        Opening.class
                )
        );

        // Check the status code of the exception
        assertEquals(409, exception.getRawStatusCode());
    }

    @Test
    public void testUnauthorizedExceptionHandlingWhenPassedEmployeeToken() {
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "abcd"); // Replace with a valid token
        headers.set("Content-Type", "application/json");

        String openingJsonBody = "{\n" +
                "    \"title\": \"Java Senior Software Engineer\",\n" +
                "    \"details\": \"Building Enterprise backend\",\n" +
                "    \"level\": 1,\n" +
                "    \"location\": \"Chennai\",\n" +
                "    \"recruiter\": {\n" +
                "        \"id\": 20\n" +
                "    },\n" +
                "    \"skills\": [\n" +
                "        {\n" +
                "            \"id\": 1\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        HttpEntity<?> entity = new HttpEntity<>(openingJsonBody, headers);

        try {
            restTemplate.exchange(
                    "http://localhost:9091/api/v1/projects/7/openings",
                    HttpMethod.POST,
                    entity,
                    String.class
            );
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.UNAUTHORIZED, e.getStatusCode());
            assertTrue(e.getMessage().contains("You don't have permission to create an opening."));
        } catch (Exception e) {
            fail("Expected UnauthorizedAccessException, but got " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
}
