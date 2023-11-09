package com.example.challengespringboot.entities;


import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import com.example.challengespringboot.dtos.UsersDTO;
import com.example.challengespringboot.enums.ERole;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
// @table
public class Users implements Serializable, UserDetails { // create myUserDetail
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

    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER) // eager may use more memory and resources
    @CollectionTable(name = "user_role")
    private Set<Integer> roles = new HashSet<>(Arrays.asList(ERole.USER.getId()));

    @ManyToMany
    private List<Movie> favoriteMovies;

    public void setAuthorities(Set<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<GrantedAuthority> authorities;

    public Users(Long id, String name, String email, String password, Set<ERole> roles) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.setRoles(roles);
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
        this.setStringRoles(dto.getRoles());
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    public Set<ERole> getRoles() {
        return roles.stream().map(r -> ERole.fromId(r)).collect(Collectors.toSet());
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

    public void setRoles(Set<ERole> roles) {
        if (roles == null || roles.isEmpty())
            this.roles.clear();
        else
            this.roles = roles.stream().map(r -> r.getId()).collect(Collectors.toSet());
    }

    public void setStringRoles(Set<String> roles) {
        if (roles == null || roles.isEmpty())
            this.roles.clear();
        else
            this.roles = roles.stream().map(s -> ERole.fromDescription(s).getId()).collect(Collectors.toSet());
    }

    public void addRole(ERole role) {
        this.roles.add(role.getId());
    }



    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", email=" + email + ", roles=" + getRoles() + "]";
    }

}
