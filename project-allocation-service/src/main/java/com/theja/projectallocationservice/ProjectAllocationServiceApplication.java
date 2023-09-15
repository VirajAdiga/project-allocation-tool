package com.theja.projectallocationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the project allocation service.
 * This class serves as the entry point for the Spring Boot application.
 */
@SpringBootApplication
public class ProjectAllocationServiceApplication {

	/**
	 * The main method that starts the Spring Boot application.
	 *
	 * @param args Command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(ProjectAllocationServiceApplication.class, args);
	}
}

