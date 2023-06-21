package com.danfoss.flighter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan
public class FlighterApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlighterApplication.class, args);
	}

}
