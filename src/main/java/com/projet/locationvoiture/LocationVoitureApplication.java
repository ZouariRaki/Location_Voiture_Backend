package com.projet.locationvoiture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.projet.locationvoiture.repository")
@EntityScan(basePackages = "com.projet.locationvoiture.entity")public class LocationVoitureApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocationVoitureApplication.class, args);
	}

}
