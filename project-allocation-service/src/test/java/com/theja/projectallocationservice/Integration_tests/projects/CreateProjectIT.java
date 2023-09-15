package com.theja.projectallocationservice.Integration_tests.projects;

import com.theja.projectallocationservice.config.RestTemplateConfig;
import com.theja.projectallocationservice.exceptions.CustomResponseErrorHandler;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.fail;

@SpringBootTest(classes = RestTemplateConfig.class)
public class CreateProjectIT {

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

    @Test
    public void testEmployeeCreateProjectThrowsUnauthorizedException() {
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
        String token = testTokens.getProperty("employee_token");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-Type", "application/json");

        String projectJsonBody = "{\n" +
                "    \"title\": \"Thomson Reuters\",\n" +
                "    \"details\": \"News Agency company\",\n" +
                "}";
        HttpEntity<?> entity = new HttpEntity<>(projectJsonBody, headers);

        try {
            restTemplate.exchange(
                    "http://localhost:9091/api/v1/projects/",
                    HttpMethod.POST,
                    entity,
                    String.class
            );
        } catch (HttpClientErrorException e) {
            System.out.println("Exception: "+ e);
            assertEquals(HttpStatus.UNAUTHORIZED, e.getStatusCode());
        } catch (Exception e) {
            fail("Expected UnauthorizedAccessException, but got " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

}
