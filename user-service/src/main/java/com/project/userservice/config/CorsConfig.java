package com.project.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
        configuration.addAllowedOrigin("http://localhost:3000"); // Allow requests from this origin
        configuration.addAllowedMethod("*"); // Allow all HTTP methods
        configuration.addAllowedHeader("*"); // Allow all headers
        configuration.setAllowCredentials(true); // Allow including credentials like cookies or authorization headers

        // Create a UrlBasedCorsConfigurationSource and register the CorsConfiguration for all paths.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
