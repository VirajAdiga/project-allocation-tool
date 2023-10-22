package com.theja.projectallocationservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Objects;

@Configuration
@EnableRabbit
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
    public Queue queue() {
        return new Queue("email-queue", false);
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
