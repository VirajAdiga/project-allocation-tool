package com.project.emailservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Objects;
import java.util.Properties;

@Configuration
public class EmailConfig {

    @Autowired
    private Environment environment;

    private String getHost(){
        return environment.getProperty("SMTP_HOST");
    }

    private Integer getPort(){
        return Integer.valueOf(Objects.requireNonNull(environment.getProperty("SMTP_PORT")));
    }

    private String getUsername(){
        return environment.getProperty("SMTP_USERNAME");
    }

    private String getPassword(){
        return environment.getProperty("SMTP_PASSWORD");
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(getHost());
        mailSender.setPort(getPort());
        mailSender.setUsername(getUsername());
        mailSender.setPassword(getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        return mailSender;
    }
}
