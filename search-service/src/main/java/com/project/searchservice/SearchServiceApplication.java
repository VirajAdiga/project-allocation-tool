package com.project.searchservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the email service.
 * This class serves as the entry point for the Spring Boot application.
 */
@SpringBootApplication
@EnableRabbit
public class SearchServiceApplication {

	/**
	 * The main method that starts the Spring Boot application.
	 *
	 * @param args Command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		// Start the Spring Boot application
		SpringApplication.run(SearchServiceApplication.class, args);
	}
}
