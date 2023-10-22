package com.theja.projectallocationservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theja.projectallocationservice.dto.EmailMessage;
import com.theja.projectallocationservice.exceptions.PublishMessageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitmqMessageService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    public void sendMessageToQueue(EmailMessage emailMessage){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String payloadString = objectMapper.writeValueAsString(emailMessage);
            rabbitTemplate.convertAndSend(queue.getName(), emailMessage);
        }
        catch (Exception exception){
            log.info(exception.getMessage());
            throw new PublishMessageException("Error publishing the message");
        }
    }
}
