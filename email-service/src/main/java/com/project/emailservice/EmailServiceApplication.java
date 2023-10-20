package com.project.emailservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the email service.
 * This class serves as the entry point for the Spring Boot application.
 */
@SpringBootApplication
public class EmailServiceApplication {

	/**
	 * The main method that starts the Spring Boot application.
	 *
	 * @param args Command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		// Start the Spring Boot application
		SpringApplication.run(EmailServiceApplication.class, args);
	}
}
