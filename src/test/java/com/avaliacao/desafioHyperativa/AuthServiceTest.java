package com.avaliacao.desafioHyperativa;

import com.avaliacao.desafioHyperativa.security.JwtUtil;
import com.avaliacao.desafioHyperativa.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) // Adicione esta linha
public class AuthServiceTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authenticationService;


    @Test
    void testAuthenticateAndGenerateToken_ValidCredentials() {
        String username = "testUser";
        String password = "testPassword";
        String expectedToken = "generatedToken";

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        Mockito.<Collection<? extends GrantedAuthority>>when(authentication.getAuthorities()).thenReturn(authorities); // Força o tipo genérico

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        when(jwtUtil.generateToken(username, "ROLE_USER")).thenReturn(expectedToken);

        String actualToken = authenticationService.authenticateAndGenerateToken(username, password);

        assertEquals(expectedToken, actualToken);
    }

    @Test
    void testAuthenticateAndGenerateToken_InvalidCredentials() {
        String username = "wrongUser";
        String password = "wrongPassword";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        assertThrows(RuntimeException.class, () -> {
            authenticationService.authenticateAndGenerateToken(username, password);
        });
    }
}