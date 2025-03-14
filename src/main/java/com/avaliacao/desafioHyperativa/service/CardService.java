package com.avaliacao.desafioHyperativa.service;

import com.avaliacao.desafioHyperativa.dto.CardDTO;
import com.avaliacao.desafioHyperativa.dto.CardCreateDTO;
import com.avaliacao.desafioHyperativa.exception.CardNotFoundException;
import com.avaliacao.desafioHyperativa.exception.UnauthorizedCardAccessException;
import com.avaliacao.desafioHyperativa.mapper.CardMapper;
import com.avaliacao.desafioHyperativa.model.Card;
import com.avaliacao.desafioHyperativa.model.User;
import com.avaliacao.desafioHyperativa.repository.CardRepository;
import com.avaliacao.desafioHyperativa.repository.UserRepository;
import com.sun.jdi.request.DuplicateRequestException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final UserRepository userRepository;

    public CardService(CardRepository cardRepository, CardMapper cardMapper, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
        this.userRepository = userRepository;
    }

    public List<CardDTO> getCardsByUser(String username) {
        User user = userRepository.findByUsername(username);
        return cardRepository.findByUserId(user.getId()).stream()
                .map(cardMapper::entityToCardDTO)
                .collect(Collectors.toList());
    }

    public List<CardDTO> getAllCards() {
        return cardRepository.findAll().stream()
                .map(cardMapper::entityToCardDTO)
                .collect(Collectors.toList());
    }

    public void registerCard(String cardNumber, String username) {

        cardNumber =  cardNumber.replaceAll("\"", "");

        var isValidCard = validateCardNumber(cardNumber);

        if (!isValidCard)
        {
            throw new IllegalArgumentException("Invalid card number");
        }
        var key = UUID.randomUUID();
        User user = userRepository.findByUsername(username);
        Card card = new Card();
        card.setNumber(cardNumber);
        card.setUserId(user.getId());
        var cardExists = cardRepository.findByNumber(cardNumber);

        if(cardExists != null){
            throw new DuplicateRequestException("Card number already in use");
        }

        card = cardRepository.save(card);
    }

    public CardDTO searchCard(UUID key, String username) {
        User user = userRepository.findByUsername(username);
        var dbKey = key.toString();

        return cardRepository.findByCardKey(dbKey)
                .filter(card -> Long.valueOf(card.getUserId()).equals(user.getId())) // Convert long to Long
                .map(cardMapper::entityToCardDTO)
                .orElse(null);
    }

    public CardDTO updateCard(CardDTO cardDTO, String username) {
        var key = cardDTO.getKey();
        var dbKey = key.toString();
        User user = userRepository.findByUsername(username);
        Card card = cardRepository.findByCardKey(dbKey)
                .orElseThrow(() -> new CardNotFoundException("Card not found: " + key));

        if (card.getUserId() != user.getId()) { // Use == para comparar longs
            throw new UnauthorizedCardAccessException("Action not permitted.");
        }

        BeanUtils.copyProperties(cardDTO, card, "id", "userId", "key");

        Card updatedCard = cardRepository.save(card);
        return cardMapper.entityToCardDTO(updatedCard);
    }

    public boolean deleteCard(UUID key, String username) {
        User user = userRepository.findByUsername(username);
        var dbKey = key.toString();

        return cardRepository.findByCardKey(dbKey)
                .filter(card -> Long.valueOf(card.getUserId()).equals(user.getId())) // Convert long to Long
                .map(card -> {
                    cardRepository.delete(card);
                    return true;
                })
                .orElse(false);
    }

    private static boolean validateCardNumber(String cardNumber) {
        if (!cardNumber.matches("\\d+")) {
            return false;
        }

        if (cardNumber.length() != 16) {
            return false;
        }

        return true;
    }
}
