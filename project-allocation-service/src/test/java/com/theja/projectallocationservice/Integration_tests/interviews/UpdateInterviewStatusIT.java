package com.theja.projectallocationservice.Integration_tests.interviews;

import com.theja.projectallocationservice.config.RestTemplateConfig;
import com.theja.projectallocationservice.models.Interview;
import com.theja.projectallocationservice.models.InterviewStatus;
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
public class UpdateInterviewStatusIT {

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
    public void testUpdateInterviewStatusCancelledToScheduledShouldThrowBadRequest() {
        String authToken = testTokens.getProperty("recruiter_token");
        Long interviewId = 14L;
        InterviewStatus newInterviewStatus = InterviewStatus.SCHEDULED;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authToken);
        headers.set("Content-Type", "application/json");

        HttpEntity<?> entity = new HttpEntity<>("{}", headers);

        try {
            restTemplate.patchForObject(
                    "http://localhost:9091/api/v1/interviews/" + interviewId + "/status?newStatus=" + newInterviewStatus,
                    entity,
                    Interview.class
            );
        } catch (HttpClientErrorException e) {
            // Check if the status code is 400 (Bad Request)
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

    @Test
    public void testUpdateInterviewStatusCancelledToCompletedShouldThrowBadRequest() {
        String authToken = testTokens.getProperty("recruiter_token");
        Long interviewId = 14L;
        InterviewStatus newInterviewStatus = InterviewStatus.COMPLETED;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authToken);
        headers.set("Content-Type", "application/json");

        HttpEntity<?> entity = new HttpEntity<>("{}", headers);

        try {
            restTemplate.patchForObject(
                    "http://localhost:9091/api/v1/interviews/" + interviewId + "/status?newStatus=" + newInterviewStatus,
                    entity,
                    Interview.class
            );
        } catch (HttpClientErrorException e) {
            // Check if the status code is 400 (Bad Request)
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }
}
