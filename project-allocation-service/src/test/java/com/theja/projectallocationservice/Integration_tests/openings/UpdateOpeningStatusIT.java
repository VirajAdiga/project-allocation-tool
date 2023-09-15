package com.theja.projectallocationservice.Integration_tests.openings;

import com.theja.projectallocationservice.config.RestTemplateConfig;
import com.theja.projectallocationservice.models.Opening;
import com.theja.projectallocationservice.models.OpeningStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootTest(classes = RestTemplateConfig.class)
public class UpdateOpeningStatusIT {
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
    public void testUpdateOpeningStatusWorksFine() {
        String authToken = testTokens.getProperty("recruiter_token");
        Long openingId = 45L; // Replace with an actual opening ID
        OpeningStatus newOpeningStatus = OpeningStatus.CLOSED;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authToken);
        headers.set("Content-Type", "application/json");

        HttpEntity<?> entity = new HttpEntity<>("{}", headers);

        Opening updatedOpening = restTemplate.patchForObject(
                "http://localhost:9091/api/v1/openings/" + openingId + "/status?newStatus=" + newOpeningStatus,
                entity,
                Opening.class
        );

        Assertions.assertEquals(OpeningStatus.CLOSED, updatedOpening.getStatus());
        Assertions.assertNotNull(updatedOpening.getId());
    }

    @Test
    public void testUpdateOpeningStatusAlreadyClosedShouldThrowBadRequest() {
        String authToken = testTokens.getProperty("recruiter_token");
        Long openingId = 45L; // Replace with an actual opening ID
        OpeningStatus newOpeningStatus = OpeningStatus.ACTIVE;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authToken);
        headers.set("Content-Type", "application/json");

        HttpEntity<?> entity = new HttpEntity<>("{}", headers);

        try {
            restTemplate.patchForObject(
                    "http://localhost:9091/api/v1/openings/" + openingId + "/status?newStatus=" + newOpeningStatus,
                    entity,
                    Opening.class
            );
        } catch (HttpClientErrorException e) {
            // Check if the status code is 400 (Bad Request)
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

    @Test
    public void testUpdateOpeningStatusClosedToPendingShouldThrowBadRequest() {
        String authToken = testTokens.getProperty("recruiter_token");
        Long openingId = 45L; // Replace with an actual opening ID
        OpeningStatus newOpeningStatus = OpeningStatus.PENDING;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authToken);
        headers.set("Content-Type", "application/json");

        HttpEntity<?> entity = new HttpEntity<>("{}", headers);

        try {
            restTemplate.patchForObject(
                    "http://localhost:9091/api/v1/openings/" + openingId + "/status?newStatus=" + newOpeningStatus,
                    entity,
                    Opening.class
            );
        } catch (HttpClientErrorException e) {
            // Check if the status code is 400 (Bad Request)
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

    @Test
    public void testUpdateOpeningStatusClosedToActiveShouldThrowBadRequest() {
        String authToken = testTokens.getProperty("recruiter_token");
        Long openingId = 45L; // Replace with an actual opening ID
        OpeningStatus newOpeningStatus = OpeningStatus.ACTIVE;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authToken);
        headers.set("Content-Type", "application/json");

        HttpEntity<?> entity = new HttpEntity<>("{}", headers);

        try {
            restTemplate.patchForObject(
                    "http://localhost:9091/api/v1/openings/" + openingId + "/status?newStatus=" + newOpeningStatus,
                    entity,
                    Opening.class
            );
        } catch (HttpClientErrorException e) {
            // Check if the status code is 400 (Bad Request)
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

}
