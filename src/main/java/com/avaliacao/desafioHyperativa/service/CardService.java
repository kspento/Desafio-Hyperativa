package com.avaliacao.desafioHyperativa.service;

import com.avaliacao.desafioHyperativa.config.EncryptionConfig;
import com.avaliacao.desafioHyperativa.dto.CardDTO;
import com.avaliacao.desafioHyperativa.dto.CardCreateDTO;
import com.avaliacao.desafioHyperativa.exception.CardNotFoundException;
import com.avaliacao.desafioHyperativa.exception.DecryptionException;
import com.avaliacao.desafioHyperativa.exception.EncryptionException;
import com.avaliacao.desafioHyperativa.exception.UnauthorizedCardAccessException;
import com.avaliacao.desafioHyperativa.mapper.CardMapper;
import com.avaliacao.desafioHyperativa.model.Card;
import com.avaliacao.desafioHyperativa.model.User;
import com.avaliacao.desafioHyperativa.repository.CardRepository;
import com.avaliacao.desafioHyperativa.repository.UserRepository;

import com.sun.jdi.request.DuplicateRequestException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Service
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardMapper cardMapper;
    private final EncryptionService encriptionService;

    public CardService(CardRepository cardRepository, UserRepository userRepository, CardMapper cardMapper, EncryptionConfig encryptionConfig, EncryptionService encriptionService) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.encriptionService = encriptionService;
        this.cardMapper = cardMapper;
    }

    public UUID registerCard(CardCreateDTO cardCreateDTO, String username) {
        User user = userRepository.findByUsername(username);
        Card card = new Card();
        card.setCvv(encriptionService.encrypt(cardCreateDTO.getCvv()));
        card.setNumber(encriptionService.encrypt(cardCreateDTO.getNumber()));
        card.setHolderName(encriptionService.encrypt(cardCreateDTO.getHolderName()));
        card.setExpirationDate(cardCreateDTO.getExpirationDate());
        card.setUserId(user.getId());
        cardRepository.save(card);

        return card.getCardKey();
    }

    public List<CardDTO> getCardsByUser(String username) {
        User user = userRepository.findByUsername(username);
        List<Card> cards = cardRepository.findByUserId(user.getId());
        return cards.stream()
                .map(card -> {
                    CardDTO cardDTO = cardMapper.entityToCardDTO(card);
                    encriptionService.decryptCardData(cardDTO);
                    return cardDTO;
                })
                .collect(Collectors.toList());
    }

    public List<CardDTO> getAllCards() {
        List<Card> cards = cardRepository.findAll();
        return cards.stream()
                .map(card -> {
                    CardDTO cardDTO = cardMapper.entityToCardDTO(card);
                    encriptionService.decryptCardData(cardDTO);
                    return cardDTO;
                })
                .collect(Collectors.toList());
    }

    public CardDTO searchCard(UUID key, String username) {
        User user = userRepository.findByUsername(username);

        Card card = cardRepository.findByCardKeyAndUserId(key.toString(), user.getId());
        if (card == null) {
            throw new CardNotFoundException("Card not found: " + key);
        }
        CardDTO cardDTO = cardMapper.entityToCardDTO(card);
        encriptionService.decryptCardData(cardDTO);
        return cardDTO;
    }

    public void updateCard(CardDTO cardDTO, String username) {
        User user = userRepository.findByUsername(username);

        Card card = cardRepository.findByCardKeyAndUserId(cardDTO.getKey().toString(), user.getId());
        if (card == null) {
            throw new CardNotFoundException("Card not found: " + cardDTO.getKey());
        }
        card.setNumber(encriptionService.encrypt(cardDTO.getNumber()));
        card.setHolderName(encriptionService.encrypt(cardDTO.getHolderName()));
        card.setCvv(encriptionService.encrypt(cardDTO.getCvv()));
        card.setExpirationDate(cardDTO.getExpirationDate());
        cardRepository.save(card);
    }

    public boolean deleteCard(UUID key, String username) {
        User user = userRepository.findByUsername(username);

        Card card = cardRepository.findByCardKeyAndUserId(key.toString(), user.getId());
        if (card == null) {
            throw new CardNotFoundException("Card not found: " + key);
        }
        cardRepository.delete(card);
        return true;
    }

    @Transactional
    public List<UUID> registerCardsFromTxt(MultipartFile file, String username) throws IOException {
        User user = userRepository.findByUsername(username);
        List<CardCreateDTO> cardCreateDTOs = parseTxtFile(file);
        List<UUID> registeredCardKeys = new ArrayList<>();
        for (CardCreateDTO dto : cardCreateDTOs) {
            if (cardRepository.findByNumber(encriptionService.encrypt(dto.getNumber())) != null) {
                throw new DuplicateRequestException("Card with number " + dto.getNumber() + " already exists.");
            }
            Card card = new Card();
            card.setCvv(encriptionService.encrypt(dto.getCvv()));
            card.setNumber(encriptionService.encrypt(dto.getNumber()));
            card.setHolderName(encriptionService.encrypt(dto.getHolderName()));
            card.setExpirationDate(dto.getExpirationDate());
            card.setUserId(user.getId());
            cardRepository.save(card);
            registeredCardKeys.add(card.getCardKey());
        }
        return registeredCardKeys;
    }

    private List<CardCreateDTO> parseTxtFile(MultipartFile file) throws IOException {
        List<CardCreateDTO> cardCreateDTOs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    CardCreateDTO dto = new CardCreateDTO();
                    dto.setNumber(parts[0]);
                    dto.setHolderName(parts[1]);
                    dto.setExpirationDate(LocalDate.parse(parts[2]));
                    dto.setCvv(parts[3]);
                    cardCreateDTOs.add(dto);
                }
            }
        }
        return cardCreateDTOs;
    }
}
