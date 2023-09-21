package com.project.userservice.services;

import com.project.userservice.dto.Skill;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProjectAllocationServiceClientImpl implements ProjectAllocationServiceClient {

    @Override
    public Skill getSkill(Long skillId) {
        String url = "http://localhost:9091/api/v1/skills/" + skillId ;
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Object> skillObject = new RestTemplate().exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                Object.class
        );

        return (Skill) skillObject.getBody();
    }
}
