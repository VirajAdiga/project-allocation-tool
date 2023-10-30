package com.project.searchservice.listeners;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.searchservice.dto.OpeningSearchMessage;
import com.project.searchservice.exception.ServerSideGeneralException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SearchMessageListener implements MessageListener {

    @Override
    @RabbitListener(queues = "#{queue.name}")
    public void onMessage(Message message) {
        try {
            log.info("Consuming the message");
            ObjectMapper objectMapper = new ObjectMapper();
            TypeReference<OpeningSearchMessage> mapType = new TypeReference<>() {};
            OpeningSearchMessage openingSearchMessage = objectMapper.readValue(message.getBody(), mapType);

            log.info("Created search index successfully");
        }
        catch (Exception e) {
            log.info("Error creating search index: " + e.getMessage());
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }
}
