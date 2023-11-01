package com.example.challengespringboot.repositories;


import java.util.Optional;

import com.example.challengespringboot.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);
}
