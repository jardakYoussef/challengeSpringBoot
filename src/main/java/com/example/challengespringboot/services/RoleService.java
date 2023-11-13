package com.example.challengespringboot.services;

import com.example.challengespringboot.entities.Role;
import com.example.challengespringboot.entities.Users;
import com.example.challengespringboot.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role create(Role role) throws Exception {
        return roleRepository.save(role);
    }

    public Role findByName(String name ){
        return roleRepository.findByName(name).orElseThrow();
    }

    public boolean existsByName(String roleName) {
        return roleRepository.existsByName(roleName).orElseThrow();
    }
}
