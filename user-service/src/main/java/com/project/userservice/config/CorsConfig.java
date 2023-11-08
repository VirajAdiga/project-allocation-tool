package com.project.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

/**
 * Configuration class for configuring CORS (Cross-Origin Resource Sharing) settings.
 */
@Configuration
public class CorsConfig {
    /**
     * Define a bean for CorsConfigurationSource to configure CORS rules.
     *
     * @return CorsConfigurationSource with configured CORS settings.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // Create a CorsConfiguration instance to define CORS rules.
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        // Create a UrlBasedCorsConfigurationSource and register the CorsConfiguration for all paths.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
