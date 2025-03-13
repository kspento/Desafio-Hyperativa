package com.avaliacao.desafioHyperativa.controller;

import com.avaliacao.desafioHyperativa.model.LoginRequest;
import com.avaliacao.desafioHyperativa.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {

        var username = loginRequest.getUsername();
        var password = loginRequest.getPassword();

        var passwordAuthentication = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManager.authenticate(
                passwordAuthentication
        );

        String jwt = jwtUtil.generateToken(authentication.getName(), authentication.getAuthorities().iterator().next().getAuthority());
        return ResponseEntity.ok(jwt);
    }
}

