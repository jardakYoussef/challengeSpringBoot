package com.example.challengespringboot.services;

import com.example.challengespringboot.entities.Users;
import com.example.challengespringboot.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

	@Autowired
	private UsersService usersService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public void initializeDatabase() throws Exception {

		System.out.println("Initializing database...");
		
		final Users user1 = new Users("youssef", "youssef@gmail.com", passwordEncoder.encode("111"));
		final Users user2 = new Users("Jardak", "Jardak@gmail.com", passwordEncoder.encode("222"));
		final Users admin = new Users("test1", "test12@gmail.com", passwordEncoder.encode("333"));
		
		admin.addRole(Role.ADMIN);
	
		System.out.println(usersService.create(user1));
		System.out.println(usersService.create(user2));
		System.out.println(usersService.create(admin));
		
		System.out.println("Database initialized!");
	}
}
