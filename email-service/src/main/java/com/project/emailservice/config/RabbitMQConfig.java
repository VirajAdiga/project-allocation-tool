package com.project.emailservice.config;

import com.project.emailservice.dto.EmailMessage;
import com.project.emailservice.listeners.EmailMessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
public class RabbitMQConfig {

    @Autowired
    private Environment environment;

    private String getHost(){
        return environment.getProperty("RABBIT_MQ_HOST");
    }

    private Integer getPort(){
        return Integer.valueOf(Objects.requireNonNull(environment.getProperty("RABBIT_MQ_PORT")));
    }

    private String getUsername(){
        return environment.getProperty("RABBIT_MQ_USERNAME");
    }

    private String getPassword(){
        return environment.getProperty("RABBIT_MQ_PASSWORD");
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(getHost());
        connectionFactory.setPort(getPort());
        connectionFactory.setUsername(getUsername());
        connectionFactory.setPassword(getPassword());
        return connectionFactory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory myFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Queue queue() {
        return new Queue("email-queue", false);
    }

    @Bean
    public EmailMessageListener emailMessageListener() {
        return new EmailMessageListener();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("com.theja.projectallocationservice.dto.EmailMessage", EmailMessage.class);
        idClassMapping.put("com.project.emailservice.dto.EmailMessage", EmailMessage.class);
        idClassMapping.put("__TypeId__", EmailMessage.class);
        classMapper.setIdClassMapping(idClassMapping);
        converter.setClassMapper(classMapper);
        return converter;
    }
}
