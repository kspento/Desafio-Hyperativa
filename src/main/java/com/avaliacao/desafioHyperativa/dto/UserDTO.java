package com.avaliacao.desafioHyperativa.dto;

import com.avaliacao.desafioHyperativa.model.Role;
import jakarta.persistence.Column;

import java.util.List;
import java.util.Set;

public class UserDTO {

    private String username;

    private String email;

    private Set<Role> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
