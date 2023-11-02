package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.dto.EmailMessage;
import com.theja.projectallocationservice.dto.OpeningSearchMessage;
import com.theja.projectallocationservice.exceptions.PublishMessageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitmqMessageService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    @Qualifier("emailQueue")
    private Queue emailQueue;

    @Autowired
    @Qualifier("searchQueue")
    private Queue searchQueue;

    public void sendMessageToQueue(EmailMessage emailMessage){
        try {
            rabbitTemplate.convertAndSend(emailQueue.getName(), emailMessage);
        }
        catch (Exception exception){
            log.info(exception.getMessage());
            throw new PublishMessageException("Error publishing the message");
        }
    }

    public void sendMessageToQueue(OpeningSearchMessage openingSearchMessage){
        try {
            rabbitTemplate.convertAndSend(searchQueue.getName(), openingSearchMessage);
        }
        catch (Exception exception){
            log.info(exception.getMessage());
            throw new PublishMessageException("Error publishing the message");
        }
    }
}
