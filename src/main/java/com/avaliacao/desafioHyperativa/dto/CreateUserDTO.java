package com.avaliacao.desafioHyperativa.dto;

import com.avaliacao.desafioHyperativa.model.Role;
import com.avaliacao.desafioHyperativa.model.enums.RoleType;
import jakarta.persistence.Column;

import java.util.Set;

public class CreateUserDTO {

    private String username;

    private String password;

    private String email;

    private Set<RoleType> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<RoleType> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleType> roles) {
        this.roles = roles;
    }
}
