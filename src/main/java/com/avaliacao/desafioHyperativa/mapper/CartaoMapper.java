package com.avaliacao.desafioHyperativa.mapper;

import com.avaliacao.desafioHyperativa.dto.CartaoDTO;
import com.avaliacao.desafioHyperativa.model.Cartao;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;


@Component
public class CartaoMapper {

    public CartaoDTO toDTO(Cartao cartao) {
        if (cartao == null) {
            return null;
        }

        CartaoDTO cartaoDTO = new CartaoDTO();
        cartaoDTO.setId(cartao.getId());
        cartaoDTO.setNumero(cartao.getNumero());
        // Não mapear o usuarioId para o dto.
        return cartaoDTO;
    }

    public Cartao toEntity(CartaoDTO cartaoDTO) {
        if (cartaoDTO == null) {
            return null;
        }

        Cartao cartao = new Cartao();
        cartao.setId(cartaoDTO.getId());
        cartao.setNumero(cartaoDTO.getNumero());
        // O usuarioId será setado na service.
        return cartao;
    }
}