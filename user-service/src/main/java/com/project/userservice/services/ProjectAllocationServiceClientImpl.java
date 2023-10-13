package com.project.userservice.services;

import com.project.userservice.dto.Skill;
import com.project.userservice.exception.ResourceNotFoundException;
import com.project.userservice.exception.ServiceClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class ProjectAllocationServiceClientImpl implements ProjectAllocationServiceClient {

    @Autowired
    private Environment environment;

    private String getProjectAllocationServiceHost(){
        return environment.getProperty("PROJECT_ALLOCATION_SERVICE");
    }

    @Override
    public Skill getSkill(Long skillId) {
        String url = String.format("%sapi/v1/skills/%s", getProjectAllocationServiceHost(), skillId.toString());
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Object> skillObject = new RestTemplate().exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    Object.class
            );

            return (Skill) skillObject.getBody();
        }
        catch (ResourceNotFoundException ex) {
            throw new ServiceClientException("Resource not found with id: " + skillId);
        }
        catch (HttpClientErrorException ex) {
            // Handle specific HTTP client errors (4xx)
            throw new ServiceClientException("Error communicating with the service:  " + ex.getStatusText());
        } catch (Exception ex) {
            // Handle other exceptions
            throw new ServiceClientException("An error occurred: " + ex.getMessage());
        }
    }
}
