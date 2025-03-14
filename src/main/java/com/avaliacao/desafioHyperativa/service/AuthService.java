package com.avaliacao.desafioHyperativa.service;

import com.avaliacao.desafioHyperativa.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    public String authenticateAndGenerateToken(String username, String password) {
        // Criação do token de autenticação
        var passwordAuthentication = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManager.authenticate(passwordAuthentication);

        // Obtém as permissões do usuário autenticado
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // Verifica se o usuário tem permissões e gera o token JWT
        if (authorities != null && !authorities.isEmpty()) {
            Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
            if (iterator.hasNext()) {
                GrantedAuthority authority = iterator.next();
                return jwtUtil.generateToken(authentication.getName(), authority.getAuthority());
            } else {
                throw new IllegalArgumentException("Nenhuma autoridade encontrada.");
            }
        } else {
            throw new IllegalArgumentException("Usuário não possui autorizações.");
        }
    }
}
