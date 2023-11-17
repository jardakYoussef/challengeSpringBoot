package com.example.challengespringboot.services;

import com.example.challengespringboot.entities.Movie;
import com.example.challengespringboot.entities.Role;
import com.example.challengespringboot.entities.Users;
import com.example.challengespringboot.enums.ERole;
import com.example.challengespringboot.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DatabaseService {

    @Autowired
    private UsersService usersService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MovieService movieService;

    public void initializeDatabase() throws Exception {

        System.out.println("Initializing database...");

        Role roleUser = new Role(ERole.USER.toString());
        roleService.create(roleUser);
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roleUser);
        final Users user1 = new Users(null, "youssef", "youssef@gmail.com", passwordEncoder.encode("111"), userRoles);
        final Users user2 = new Users(null, "Jardak", "Jardak@gmail.com", passwordEncoder.encode("222"), userRoles);
        final Users user3 = new Users(null, "Slim", "Slim@gmail.com", passwordEncoder.encode("222"), userRoles);
        final Users user4 = new Users(null, "test1", "test12@gmail.com", passwordEncoder.encode("333"), userRoles);
        final Users admin1 = new Users("test1", "test1234@gmail.com", passwordEncoder.encode("333"));

        Role adminRole = new Role(ERole.ADMIN.toString());
        roleService.create(adminRole);
        admin1.addRole(adminRole);


        // Load all the movies into database
        // Add favorite movies to users
        List<Movie> movies = new ArrayList<>();
        List<Movie> allMovies = movieService.getMovieFromApi();

        movies.add(allMovies.get(0));
        user1.getFavoriteMovies().addAll(movies);
        movies.add(allMovies.get(1));
        user2.getFavoriteMovies().addAll(movies);
        user2.getFavoriteMovies().add(allMovies.get(9));
        user2.getFavoriteMovies().add(allMovies.get(7));

        movies.add(allMovies.get(2));
        movies.add(allMovies.get(3));
        movies.add(allMovies.get(4));
        movies.add(allMovies.get(5));
        user3.setFavoriteMovies(movies);

        movies = new ArrayList<>();
        movies.add(allMovies.get(6));

        user4.setFavoriteMovies(movies);
     //   user4.setFavoriteMovies(user3.getFavoriteMovies());

        System.out.println(usersService.create(user1));
        System.out.println(usersService.create(user2));
        System.out.println(usersService.create(user3));
        System.out.println(usersService.create(user4));
        System.out.println(usersService.create(admin1));


        System.out.println("Database initialized!");
    }
}
