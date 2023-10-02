package com.theja.projectallocationservice.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for defining beans related to application settings.
 */
@Configuration
public class ApplicationConfig {

    @Bean
    public Dotenv dotenv(){
        return Dotenv.configure().load();
    }
}
