package com.nilga.newsparserapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.nilga.shared.model")  // Scan for entities in the shared module
@EnableJpaRepositories(basePackages = "com.nilga.newsparserapp.repository")
public class NewsParserApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsParserApplication.class, args);
	}

}
