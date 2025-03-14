package com.avaliacao.desafioHyperativa.controller;

import com.avaliacao.desafioHyperativa.model.LoginRequest;
import com.avaliacao.desafioHyperativa.security.JwtUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Iterator;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    @SecurityRequirement(name = "") // Este endpoint pode ser acessado sem token
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {

        var username = loginRequest.getUsername();
        var password = loginRequest.getPassword();

        var passwordAuthentication = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManager.authenticate(
                passwordAuthentication
        );

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        if (authorities != null && !authorities.isEmpty()) {
            Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
            if (iterator.hasNext()) { // Verifica se há um próximo elemento
                GrantedAuthority authority = iterator.next();
                String jwt = jwtUtil.generateToken(authentication.getName(), authority.getAuthority());
                return ResponseEntity.ok(jwt);
            } else {
                return ResponseEntity.badRequest().body("Nenhuma autoridade encontrada.");
            }

        } else {
            return ResponseEntity.badRequest().body("Usuário não possui autorizações.");
        }
    }
}

