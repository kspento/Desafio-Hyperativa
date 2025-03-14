package com.avaliacao.desafioHyperativa.repository;

import com.avaliacao.desafioHyperativa.model.Role;
import com.avaliacao.desafioHyperativa.model.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleType name);

    Set<Role> findByNameIn(Set<RoleType> names);

}
