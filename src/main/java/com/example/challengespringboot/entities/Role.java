package com.example.challengespringboot.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jdk.dynalink.Operation;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Entity
class Role implements GrantedAuthority {
    @Id
    private String id;


    @Override
    public String getAuthority() {
        return id;
    }


}