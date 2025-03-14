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
        cardCreateDTO.setNumber(card.getNumber());
        cardCreateDTO.setHolderName(card.getHolderName());
        cardCreateDTO.setExpirationDate(card.getExpirationDate());
        cardCreateDTO.setCvv(card.getCvv());
        return cardCreateDTO;
    }

    public CardDTO entityToCardDTO(Card card) {
        if (card == null) {
            return null;
        }

        CardDTO cardDTO = new CardDTO();
        cardDTO.setKey(card.getCardKey());
        cardDTO.setNumber(card.getNumber());
        cardDTO.setHolderName(card.getHolderName());
        cardDTO.setExpirationDate(card.getExpirationDate());
        cardDTO.setCvv(card.getCvv());
        return cardDTO;
    }

    public Card CardCreateDTOToEntity(CardCreateDTO cardCreateDTO) {
        if (cardCreateDTO == null) {
            return null;
        }

        Card card = new Card();
        card.setNumber(cardCreateDTO.getNumber());
        card.setHolderName(cardCreateDTO.getHolderName());
        card.setExpirationDate(cardCreateDTO.getExpirationDate());
        card.setCvv(cardCreateDTO.getCvv());
        return card;
    }
}