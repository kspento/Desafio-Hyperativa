package com.avaliacao.desafioHyperativa.service;

import com.avaliacao.desafioHyperativa.model.Usuario;
import com.avaliacao.desafioHyperativa.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario cadastrarUsuario(Usuario usuario) {
        // Implementar lógica de criptografia de senha
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> obterTodos() {
        return usuarioRepository.findAll();
    }
}
