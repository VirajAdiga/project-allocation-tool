package com.project.emailservice.listeners;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.emailservice.dto.EmailMessage;
import com.project.emailservice.exception.EmailCodeNotAvailableException;
import com.project.emailservice.exception.SendMailException;
import com.project.emailservice.mappers.EmailCodeToMessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailMessageListener implements MessageListener {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailCodeToMessageMapper emailCodeToMessageMapper;

    @Override
    @RabbitListener(queues = "#{queue.name}")
    public void onMessage(Message message) {
        try {
            log.info("Consuming the message");
            ObjectMapper objectMapper = new ObjectMapper();
            TypeReference<EmailMessage> mapType = new TypeReference<>() {};
            EmailMessage emailMessage = objectMapper.readValue(message.getBody(), mapType);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(emailMessage.getRecipient());
            mailMessage.setSubject(emailMessage.getSubject());
            mailMessage.setText(emailCodeToMessageMapper.getEmailBody(emailMessage.getBody()));

            mailSender.send(mailMessage);

            log.info("Email sent successfully.");
        } catch (EmailCodeNotAvailableException exception) {
            log.info("Error " + exception.getMessage());
        }
        catch (Exception e) {
            log.info("Error sending email: " + e.getMessage());
            throw new SendMailException("Error sending the mail");
        }
    }
}
