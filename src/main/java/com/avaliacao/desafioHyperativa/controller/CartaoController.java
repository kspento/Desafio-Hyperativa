package com.avaliacao.desafioHyperativa.controller;

import com.avaliacao.desafioHyperativa.dto.CartaoDTO;
import com.avaliacao.desafioHyperativa.service.CartaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/cartoes")
public class CartaoController {

    private final CartaoService cartaoService;

    public CartaoController(CartaoService cartaoService) {
        this.cartaoService = cartaoService;
    }

    @GetMapping("/meus-cartoes")
    public ResponseEntity<List<CartaoDTO>> listarMeusCartoes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<CartaoDTO> cartoes = cartaoService.listarCartoesPorUsuario(username);
        return ResponseEntity.ok(cartoes);
    }

    @GetMapping("/todos-cartoes")
    public ResponseEntity<List<CartaoDTO>> listarTodosCartoes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<CartaoDTO> cartoes = cartaoService.listarTodosCartoes();
        return ResponseEntity.ok(cartoes);
    }

    @PostMapping
    public ResponseEntity<CartaoDTO> cadastrarCartao(@RequestBody CartaoDTO cartaoDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CartaoDTO cartao = cartaoService.cadastrarCartao(cartaoDTO, authentication.getName());
        return ResponseEntity.ok(cartao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartaoDTO> buscarCartao(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CartaoDTO cartao = cartaoService.buscarCartao(id, authentication.getName());
        if (cartao == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cartao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartaoDTO> atualizarCartao(@PathVariable Long id, @RequestBody CartaoDTO cartaoDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CartaoDTO cartao = cartaoService.atualizarCartao(id, cartaoDTO, authentication.getName());
        if (cartao == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cartao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCartao(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!cartaoService.excluirCartao(id, authentication.getName())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}