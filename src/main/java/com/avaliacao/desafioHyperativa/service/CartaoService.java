package com.avaliacao.desafioHyperativa.service;

import com.avaliacao.desafioHyperativa.dto.CartaoDTO;
import com.avaliacao.desafioHyperativa.dto.CartaoRequestDto;
import com.avaliacao.desafioHyperativa.mapper.CartaoMapper;
import com.avaliacao.desafioHyperativa.model.Cartao;
import com.avaliacao.desafioHyperativa.model.Usuario;
import com.avaliacao.desafioHyperativa.repository.CartaoRepository;
import com.avaliacao.desafioHyperativa.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartaoService {

    private final CartaoRepository cartaoRepository;
    private final CartaoMapper cartaoMapper;
    private final UsuarioRepository usuarioRepository;

    public CartaoService(CartaoRepository cartaoRepository, CartaoMapper cartaoMapper, UsuarioRepository usuarioRepository) {
        this.cartaoRepository = cartaoRepository;
        this.cartaoMapper = cartaoMapper;
        this.usuarioRepository = usuarioRepository;
    }

    public List<CartaoDTO> listarCartoesPorUsuario(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username);
        return cartaoRepository.findByUsuarioId(usuario.getId()).stream()
                .map(cartaoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<CartaoDTO> listarTodosCartoes() {
        return cartaoRepository.findAll().stream()
                .map(cartaoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CartaoDTO cadastrarCartao(CartaoDTO cartaoDTO, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username);
        Cartao cartao = cartaoMapper.toEntity(cartaoDTO);
        cartao.setUsuarioId(usuario.getId());
        return cartaoMapper.toDTO(cartaoRepository.save(cartao));
    }

    public CartaoDTO buscarCartao(Long id, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username);
        return cartaoRepository.findById(id)
                .filter(cartao -> Long.valueOf(cartao.getUsuarioId()).equals(usuario.getId())) // Convert long to Long
                .map(cartaoMapper::toDTO)
                .orElse(null);
    }

    public CartaoDTO atualizarCartao(Long id, CartaoDTO cartaoDTO, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username);
        return cartaoRepository.findById(id)
                .filter(cartao -> Long.valueOf(cartao.getUsuarioId()).equals(usuario.getId())) // Convert long to Long
                .map(cartao -> {
                    Cartao cartaoAtualizado = cartaoMapper.toEntity(cartaoDTO);
                    cartaoAtualizado.setId(id);
                    cartaoAtualizado.setUsuarioId(usuario.getId());
                    return cartaoMapper.toDTO(cartaoRepository.save(cartaoAtualizado));
                })
                .orElse(null);
    }

    public boolean excluirCartao(Long id, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username);
        return cartaoRepository.findById(id)
                .filter(cartao -> Long.valueOf(cartao.getUsuarioId()).equals(usuario.getId())) // Convert long to Long
                .map(cartao -> {
                    cartaoRepository.delete(cartao);
                    return true;
                })
                .orElse(false);
    }
}
