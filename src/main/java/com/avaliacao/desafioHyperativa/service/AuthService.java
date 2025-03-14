package com.avaliacao.desafioHyperativa.service;

import com.avaliacao.desafioHyperativa.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
        var passwordAuthentication = new UsernamePasswordAuthenticationToken(username, password);


        try {
            Authentication authentication = authenticationManager.authenticate(passwordAuthentication);
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (authorities != null && !authorities.isEmpty()) {
                Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
                if (iterator.hasNext()) {
                    GrantedAuthority authority = iterator.next();
                    return jwtUtil.generateToken(authentication.getName(), authority.getAuthority());
                } else {
                    throw new IllegalArgumentException("No authorities found");
                }
            } else {
                throw new IllegalArgumentException("User has no authorization.");
            }
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Invalid Credentials.");
        }
    }
}
