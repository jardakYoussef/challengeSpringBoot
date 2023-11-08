package com.example.challengespringboot.config;

import com.example.challengespringboot.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TestConfig {
	
	@Autowired
	private DatabaseService databaseService;
	
	@Bean
	public void initializeDatabase() throws Exception {
		databaseService.initializeDatabase();
	}

}
