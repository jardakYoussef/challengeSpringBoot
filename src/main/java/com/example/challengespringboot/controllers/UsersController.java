package com.example.challengespringboot.controllers;

import java.util.List;
import java.util.Set;

import com.example.challengespringboot.dtos.UsersDTO;
import com.example.challengespringboot.entities.Movie;
import com.example.challengespringboot.entities.Users;
import com.example.challengespringboot.enums.ERole;
import com.example.challengespringboot.services.UsersService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Transactional
public class UsersController {

    @Autowired
    private UsersService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<UsersDTO>> findAll() {
        final List<Users> users = userService.findAll();
        final List<UsersDTO> dtos = users.stream().map(user -> new UsersDTO(user)).toList();
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<UsersDTO> create(@RequestBody UsersDTO dto) throws Exception {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        return ResponseEntity.ok(userService.create(dto));
    }

    public ResponseEntity<UsersDTO> update(@RequestBody UsersDTO dto) throws Exception {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        return ResponseEntity.ok(userService.create(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/admin/{userId}/upgrade-permissions")
    public ResponseEntity<UsersDTO> updateUserRoles(
            @PathVariable Long userId,
            @RequestBody Set<String> newRoles
    ) throws Exception {
        Users updatedUser = userService.updateUserRoles(userId, newRoles);
        UsersDTO userDTOUpdated = new UsersDTO();
        userDTOUpdated.setRoles(updatedUser.getRoles());
        userDTOUpdated.setName(updatedUser.getName());
        userDTOUpdated.setEmail(updatedUser.getEmail());

        return ResponseEntity.ok(userDTOUpdated);
    }

    @RateLimiter(name = "backendA")
    @PutMapping("/{userId}/favorites")
    public List<Movie> addMovieToFavorites(@PathVariable Long userId, @RequestBody List<Long> movieIds) throws Exception {
        return userService.addMovieToFavorites(userId, movieIds);
    }

    @PutMapping("/{userId}/removeFavorite")
    public List<Movie> removeMovieFromFavorites(@PathVariable Long userId, @RequestBody List<Long> movieIds) throws Exception {
        return userService.removeMovieFromFavorites(userId, movieIds);
    }
    @Transactional
    @GetMapping("/{userId}/favoriteMoviesPerUser")
    public List<Movie> favoriteMoviesPerUser(@PathVariable Long userId){
        return userService.favoriteMoviesPerUser(userId);

    }
}
