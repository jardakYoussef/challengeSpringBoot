package com.example.challengespringboot.repositories;


import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.example.challengespringboot.entities.Role;
import com.example.challengespringboot.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

    Optional <List<Users>> findByRolesContaining(Role role);
}
