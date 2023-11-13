package com.example.challengespringboot.services;

import com.example.challengespringboot.entities.Role;
import com.example.challengespringboot.entities.Users;
import com.example.challengespringboot.enums.ERole;
import com.example.challengespringboot.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class DatabaseService {

    @Autowired
    private UsersService usersService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    public void initializeDatabase() throws Exception {

        System.out.println("Initializing database...");

        Role roleUser = new Role(ERole.USER.toString());
        roleService.create(roleUser);
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roleUser);
        final Users user1 = new Users(null, "youssef", "youssef@gmail.com", passwordEncoder.encode("111"), userRoles);
        final Users user2 = new Users(null, "Jardak", "Jardak@gmail.com", passwordEncoder.encode("222"), userRoles);
        final Users admin = new Users("test1", "test12@gmail.com", passwordEncoder.encode("333"));
        final Users admin1 = new Users("test1", "test1234@gmail.com", passwordEncoder.encode("333"));

        Role adminRole = new Role(ERole.ADMIN.toString());
        roleService.create(adminRole);
        admin.addRole(adminRole);
        admin1.addRole(adminRole);

        System.out.println(usersService.create(user1));
        System.out.println(usersService.create(user2));
        System.out.println(usersService.create(admin));
        System.out.println(usersService.create(admin1));

        System.out.println("Database initialized!");
    }
}
