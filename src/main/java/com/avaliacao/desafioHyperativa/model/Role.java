package com.avaliacao.desafioHyperativa.model;

import com.avaliacao.desafioHyperativa.model.enums.RoleType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;

@Entity
@Data
@ToString(exclude = "usuarios") // Exclui o campo 'usuarios'
@EqualsAndHashCode(exclude = "usuarios") // Exclui o campo 'usuarios'
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleType name;

    // Se Usuario tiver um relacionamento bidirecional:
    @ManyToMany(mappedBy = "roles")
    private Set<Usuario> usuarios;
}