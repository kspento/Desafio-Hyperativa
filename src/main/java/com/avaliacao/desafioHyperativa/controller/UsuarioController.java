package com.avaliacao.desafioHyperativa.controller;

import com.avaliacao.desafioHyperativa.model.Usuario;
import com.avaliacao.desafioHyperativa.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public Usuario cadastrarUsuario(@RequestBody Usuario usuario) {
        return usuarioService.cadastrarUsuario(usuario);
    }

    @GetMapping
    public List<Usuario> ObterTodos()
    {
        return usuarioService.obterTodos();
    }
}