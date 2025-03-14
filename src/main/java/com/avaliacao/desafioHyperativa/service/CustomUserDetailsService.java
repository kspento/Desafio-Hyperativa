package com.avaliacao.desafioHyperativa.service;
import com.avaliacao.desafioHyperativa.model.Usuario;
import com.avaliacao.desafioHyperativa.repository.UsuarioRepository;
import com.avaliacao.desafioHyperativa.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = usuarioRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
        return new CustomUserDetails(user); // Use um objeto UserDetails personalizado
    }
}
