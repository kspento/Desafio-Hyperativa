package com.avaliacao.desafioHyperativa.config.initializer;

import com.avaliacao.desafioHyperativa.model.Role;
import com.avaliacao.desafioHyperativa.model.Usuario;
import com.avaliacao.desafioHyperativa.model.enums.RoleType;
import com.avaliacao.desafioHyperativa.repository.RoleRepository;
import com.avaliacao.desafioHyperativa.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository.*;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UsuarioRepository usuarioRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            Role adminRole = new Role();
            adminRole.setName(RoleType.ADMIN);
            roleRepository.save(adminRole);

            Role userRole = new Role();
            userRole.setName(RoleType.USER);
            roleRepository.save(userRole);
        }

        if (usuarioRepository.findByUsername("admin") == null) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(roleRepository.findByName(RoleType.ADMIN));
            admin.setRoles(adminRoles);
            usuarioRepository.save(admin);
        }

        if (usuarioRepository.findByUsername("user") == null) {
            Usuario user = new Usuario();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user"));
            Set<Role> userRoles = new HashSet<>();
            userRoles.add(roleRepository.findByName(RoleType.USER));
            user.setRoles(userRoles);
            usuarioRepository.save(user);
        }
    }
}
