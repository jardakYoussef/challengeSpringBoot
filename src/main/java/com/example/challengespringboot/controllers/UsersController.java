package com.example.challengespringboot.controllers;

import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Set;

import com.example.challengespringboot.dtos.UsersDTO;
import com.example.challengespringboot.entities.Users;
import com.example.challengespringboot.enums.Role;
import com.example.challengespringboot.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<UsersDTO>> findAll() {
        final List<Users> users = service.findAll();
        final List<UsersDTO> dtos = users.stream().map(user -> new UsersDTO(user)).toList();
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<UsersDTO> create(@RequestBody UsersDTO dto) throws Exception {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        return ResponseEntity.ok(service.create(dto));
    }

    public ResponseEntity<UsersDTO> update(@RequestBody UsersDTO dto) throws Exception {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        return ResponseEntity.ok(service.create(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    @Secured("ADMIN")
    @PutMapping("/{userId}/upgrade-permissions")
    public ResponseEntity<UsersDTO> updateUserRoles(
            @PathVariable Long userId,
            @RequestBody Set<Role> newRoles
    ) {
        Users updatedUser = service.updateUserRoles(userId, newRoles);
        UsersDTO userDTOUpdated= new UsersDTO();
        userDTOUpdated.setRoles(updatedUser.getRoles());
        userDTOUpdated.setName(updatedUser.getName());
        userDTOUpdated.setEmail(updatedUser.getEmail());

        return ResponseEntity.ok(userDTOUpdated);
    }

}
