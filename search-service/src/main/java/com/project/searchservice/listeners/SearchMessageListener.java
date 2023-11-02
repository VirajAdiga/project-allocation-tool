package com.project.searchservice.listeners;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.searchservice.dto.OpeningSearchMessage;
import com.project.searchservice.entities.OpeningSearch;
import com.project.searchservice.entities.enums.SearchRepoActionType;
import com.project.searchservice.exception.InvalidActionTypeException;
import com.project.searchservice.exception.ServerSideGeneralException;
import com.project.searchservice.services.OpeningSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SearchMessageListener implements MessageListener {

    @Autowired
    private OpeningSearchService openingSearchService;

    @Override
    @RabbitListener(queues = "#{queue.name}")
    public void onMessage(Message message) {
        try {
            log.info("Consuming the message");
            ObjectMapper objectMapper = new ObjectMapper();
            TypeReference<OpeningSearchMessage> mapType = new TypeReference<>() {};
            OpeningSearchMessage openingSearchMessage = objectMapper.readValue(message.getBody(), mapType);
            SearchRepoActionType actionType = Enum.valueOf(SearchRepoActionType.class, openingSearchMessage.getActionType());
            OpeningSearch openingSearch = OpeningSearch.builder().
                    id(openingSearchMessage.getId()).
                    title(openingSearchMessage.getTitle()).
                    details(openingSearchMessage.getDetails()).
                    level(openingSearchMessage.getLevel()).
                    location(openingSearchMessage.getLocation()).
                    status(openingSearchMessage.getStatus()).
                    projectName(openingSearchMessage.getProjectName()).
                    skills(openingSearchMessage.getSkills()).
                    build();
            switch (actionType){
                case CREATE -> openingSearchService.createOpeningForSearch(openingSearch);
                case DELETE -> openingSearchService.deleteOpening(openingSearch);
            }
            log.info("Consumed message successfully");
        }catch (IllegalArgumentException exception){
            throw new InvalidActionTypeException("Invalid action type");
        }
        catch (Exception e) {
            log.info("Error consuming the message: " + e.getMessage());
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }
}
