package com.example.challengespringboot.dtos;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.challengespringboot.entities.Role;
import com.example.challengespringboot.entities.Users;
import com.example.challengespringboot.enums.ERole;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UsersDTO {

    private Long id;
    private String name;
    private String email;
    @JsonIgnore

    private String password;
    private Set<Role> roles = new HashSet<>();

    public UsersDTO() {
        super();
    }

    public UsersDTO(Long id, String name, String email, Set<Role> roles) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.roles = roles;
    }

    public UsersDTO(Users user) {
        super();
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.setRoles(user.getRoles());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


}
