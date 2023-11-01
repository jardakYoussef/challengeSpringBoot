package com.example.challengespringboot.config;

import com.example.challengespringboot.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {
	
	@Autowired
	private DatabaseService databaseService;
	
	@Bean
	public void initializeDatabase() throws Exception {
		databaseService.initializeDatabase();
	}

}
