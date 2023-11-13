package com.example.challengespringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching

public class ChallengeSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChallengeSpringBootApplication.class, args);
	}

}
