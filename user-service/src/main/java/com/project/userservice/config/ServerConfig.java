package com.project.userservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Objects;

@Configuration
public class ServerConfig {

    @Autowired
    private Environment environment;

    private Integer getServerPort(){
        return Integer.valueOf(Objects.requireNonNull(environment.getProperty("SERVER_PORT")));
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
        return factory -> factory.setPort(getServerPort());
    }
}
