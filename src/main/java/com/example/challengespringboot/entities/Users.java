package com.example.challengespringboot.entities;


import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import com.example.challengespringboot.dtos.UsersDTO;
import com.example.challengespringboot.enums.ERole;
import jakarta.persistence.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
// @table
public class Users implements Serializable { // create myUserDetail
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen_users_id")
    @SequenceGenerator(name = "gen_users_id", sequenceName = "seq_users_id", allocationSize = 1)
    private Long id;

    public List<Movie> getFavoriteMovies() {
        return favoriteMovies;
    }

    public void setFavoriteMovies(List<Movie> favoriteMovies) {
        this.favoriteMovies = favoriteMovies;
    }

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 60)
    private String password;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Set<Role> roles;

    private boolean enabled;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Movie> favoriteMovies;


    public void initializeFavoriteMovies() {
        Hibernate.initialize(favoriteMovies);
    }

    public Users(Long id, String name, String email, String password, Set<Role> roles) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.setRoles(roles);
        this.favoriteMovies= new ArrayList<>();
    }

    public Users(String name, String email, String password) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Users(UsersDTO dto) {
        this(dto.getName(), dto.getEmail(), dto.getPassword());
        this.setId(dto.getId());
        // this.setRoles(dto.getRoles());
    }

    public Users() {

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


    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", enabled=" + enabled +
                '}';
    }

    public void addRole(Role role) {
        this.roles = new HashSet<>();
        roles.add(role);
    }
}
