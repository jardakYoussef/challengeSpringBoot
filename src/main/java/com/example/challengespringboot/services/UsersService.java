package com.example.challengespringboot.services;


import java.util.*;

import com.example.challengespringboot.dtos.UsersDTO;
import com.example.challengespringboot.entities.Movie;
import com.example.challengespringboot.entities.Role;
import com.example.challengespringboot.entities.Users;
import com.example.challengespringboot.enums.ERole;
import com.example.challengespringboot.repositories.MovieRepository;
import com.example.challengespringboot.repositories.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class UsersService {

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private MovieService movieService;

    @Autowired
    private RoleService roleService;


    public Users findById(Long id) {
        return userRepository.findById(id).get();
    }

    public Users findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    public List<Users> findAll() {
        return userRepository.findAll();
    }

    public Users create(Users user) throws Exception {
         checkEmailDuplication(user);
        user.setId(null);
        return userRepository.save(user);
    }

    public UsersDTO create(UsersDTO dto) throws Exception {
        return new UsersDTO(create(new Users(dto)));
    }

    public Users update(Users user) throws Exception {
        checkEmailDuplication(user);
        Users u = findById(user.getId());
        u.setName(user.getName());
        u.setEmail(user.getEmail());
        u.setRoles(user.getRoles());
        return userRepository.save(u);
    }

    public void delete(Long id) {
        final Users user = findById(id);
        userRepository.delete(user);
    }

    // thread safe java to eliminate the risk of two users creating the same account with the same email

    private void checkEmailDuplication(Users user) throws Exception {
        final String email = user.getEmail();

        if (StringUtils.hasLength(email)) {
            final Users p = userRepository.findByEmail(email).orElse(null);
            if (p != null) {
                throw new Exception("Duplicated"); // change with an exception handling
            }
        }
    }


    @Transactional  // why it's used ?
    public Users updateUserRoles(Long userId, Set<String> newRoles) throws Exception {
        // Retrieve the user by ID from the database
        Users u = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        // Validate the new roles to ensure they exist
        validateRolesExist(newRoles);

        // Update the user's roles with the new roles
        Set<Role> rolesList = new HashSet<>();
        rolesList.addAll(createRoleListFromName(newRoles));
            u.setRoles(rolesList);

        // Save the updated user
        return userRepository.save(u);
    }

    public void validateRolesExist(Set<String> newRoles) throws Exception {
        for (String roleName : newRoles) {
            if (!roleService.existsByName(roleName)) {
                throw new Exception("Role '" + roleName + "' does not exists.");
            }
        }
    }


    public Set<Role> createRoleListFromName(Set<String> roleNames) {
        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            roles.add(roleService.findByName(roleName));
        }
        return roles;
    }

    @Transactional
    public List<Movie> addMovieToFavorites(Long userId, List<Long> movieIds) throws Exception {
        Optional<Users> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new Exception("User with ID " + userId + " does not exist");
        }

        Users user = userOptional.get();

        for (Long movieId : movieIds) {
            Movie movie = movieService.findById(movieId);
            if (!user.getFavoriteMovies().contains(movie)) {
                user.getFavoriteMovies().add(movie);
                movieService.addStar(movie); // create a lock in the type and then throw an exception if more than one user try to add
                // a star to the same movie at the same time
            }
        }
        userRepository.save(user);
        return user.getFavoriteMovies();
    }

    @Transactional
    public List<Movie> removeMovieFromFavorites(Long userId, List<Long> movieIds) throws Exception {
        Optional<Users> userOptional = userRepository.findById(userId);

        if (!userOptional.isPresent()) {
            throw new Exception("User with ID " + userId + " does not exist");
        }

        Users user = userOptional.get();

        for (Long movieId : movieIds) {
            Movie movie = movieService.findById(movieId);
            if (user.getFavoriteMovies().contains(movie)) {
                user.getFavoriteMovies().remove(movie);
                movieService.removeStar(movie);
            }
        }
        userRepository.save(user);
        return user.getFavoriteMovies();
    }

@Transactional
    public List<Movie> favoriteMoviesPerUser(Long userId) {
        Users user = findById(userId);
        user.initializeFavoriteMovies();
        List<Movie> movieList = user.getFavoriteMovies();
        return movieList;
    }
}

