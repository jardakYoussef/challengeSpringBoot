package com.example.challengespringboot.services;


import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.example.challengespringboot.dtos.UsersDTO;
import com.example.challengespringboot.entities.Users;
import com.example.challengespringboot.enums.Role;
import com.example.challengespringboot.repositories.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsersService {

    @Autowired
    private UsersRepository userRepository;

    public Users findById(Long id) {
        return userRepository.findById(id).get();
    }

    public Users findByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }

    public List<Users> findAll() {
        return userRepository.findAll();
    }

    public Users create(Users user) throws Exception {
        user.setId(null);
        user.addRole(Role.USER);
        checkEmailDuplication(user);
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

    private void checkEmailDuplication(Users user) throws Exception {
        final String email = user.getEmail();
        if (email != null && email.length() > 0) {
            final Long id = user.getId();
            final Users p = userRepository.findByEmail(email).orElse(null);
            if (p != null && Objects.equals(p.getEmail(), email) && !Objects.equals(p.getId(), id)) {
                throw new Exception("Duplicated");
            }
        }
    }


    @Transactional
    public Users updateUserRoles(Long userId, Set<Role> newRoles) {
        // Retrieve the user by ID from the database
        Users u = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        // Validate the new roles to ensure they exist
        validateRolesExist(newRoles);

        // Update the user's roles with the new roles
        u.setRoles(newRoles);

        // Save the updated user
        return userRepository.save(u);
    }

    private void validateRolesExist(Set<Role> roles) {
        for (Role role : roles) {
            if (!EnumSet.of(Role.USER, Role.ADMIN).contains(role)) {
                throw new IllegalArgumentException("Invalid role: " + role);
            }
        }
    }
}

