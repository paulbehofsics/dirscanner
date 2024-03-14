package com.paulbehofsics.dirscanner.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.paulbehofsics.dirscanner")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
