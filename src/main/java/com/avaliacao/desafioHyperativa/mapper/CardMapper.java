package com.avaliacao.desafioHyperativa.mapper;

import com.avaliacao.desafioHyperativa.dto.CardDTO;
import com.avaliacao.desafioHyperativa.dto.CardCreateDTO;
import com.avaliacao.desafioHyperativa.model.Card;
import org.springframework.stereotype.Component;


@Component
public class CardMapper {

    public CardCreateDTO entityToCardCreateDTO(Card card) {
        if (card == null) {
            return null;
        }

        CardCreateDTO cardCreateDTO = new CardCreateDTO();
        cardCreateDTO.setId(card.getId());
        cardCreateDTO.setNumber(card.getNumber());
        // Não mapear o usuarioId para o dto.
        return cardCreateDTO;
    }

    public CardDTO entityToCardDTO(Card card) {
        if (card == null) {
            return null;
        }

        CardDTO cardCreateDTO = new CardDTO();
        cardCreateDTO.setKey(card.getCardKey());
        cardCreateDTO.setNumber(card.getNumber());
        // Não mapear o usuarioId para o dto.
        return cardCreateDTO;
    }

    public Card CardCreateDTOToEntity(CardCreateDTO cardCreateDTO) {
        if (cardCreateDTO == null) {
            return null;
        }

        Card card = new Card();
        card.setId(cardCreateDTO.getId());
        card.setNumber(cardCreateDTO.getNumber());
        // O usuarioId será setado na service.
        return card;
    }
}