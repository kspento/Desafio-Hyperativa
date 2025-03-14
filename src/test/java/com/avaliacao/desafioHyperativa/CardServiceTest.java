package com.avaliacao.desafioHyperativa;

import com.avaliacao.desafioHyperativa.config.EncryptionConfig;
import com.avaliacao.desafioHyperativa.dto.CardCreateDTO;
import com.avaliacao.desafioHyperativa.dto.CardDTO;
import com.avaliacao.desafioHyperativa.exception.CardNotFoundException;
import com.avaliacao.desafioHyperativa.mapper.CardMapper;
import com.avaliacao.desafioHyperativa.model.Card;
import com.avaliacao.desafioHyperativa.model.User;
import com.avaliacao.desafioHyperativa.repository.CardRepository;
import com.avaliacao.desafioHyperativa.repository.UserRepository;
import com.avaliacao.desafioHyperativa.service.CardService;
import com.avaliacao.desafioHyperativa.service.EncryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CardMapper cardMapper;

    @Mock
    private EncryptionService encryptionService;

    @Mock
    private EncryptionConfig encryptionConfig;

    @InjectMocks
    private CardService cardService;

    private User user;
    private Card card;
    private CardDTO cardDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        card = new Card();
        card.setId(1L);
        card.setNumber("encrypted_1234567812345678");
        card.setHolderName("encrypted_Test Holder");
        card.setExpirationDate(LocalDate.now().plusYears(1));
        card.setCvv("encrypted_123");
        card.setUserId(user.getId());
        card.setCardKey(UUID.randomUUID());

        cardDTO = new CardDTO();
        cardDTO.setKey(card.getCardKey());
        cardDTO.setNumber("1234567812345678");
        cardDTO.setHolderName("Test Holder");
        cardDTO.setExpirationDate(card.getExpirationDate());
        cardDTO.setCvv("123");
    }

    @Test
    void testRegisterCard_ValidCardCreateDTO() {
        CardCreateDTO cardCreateDTO = new CardCreateDTO();
        cardCreateDTO.setNumber("1234567812345678");
        cardCreateDTO.setHolderName("Test Holder");
        cardCreateDTO.setExpirationDate(LocalDate.now().plusYears(1));
        cardCreateDTO.setCvv("123");

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(encryptionService.encrypt("1234567812345678")).thenReturn("encrypted_1234567812345678");
        when(encryptionService.encrypt("Test Holder")).thenReturn("encrypted_Test Holder");
        when(encryptionService.encrypt("123")).thenReturn("encrypted_123");
        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> {
            Card savedCard = invocation.getArgument(0);
            savedCard.setCardKey(UUID.randomUUID()); // Simulate setting the cardKey after saving
            return savedCard;
        });

        UUID cardKey = cardService.registerCard(cardCreateDTO, "testUser");

        assertNotNull(cardKey);
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    void testGetCardsByUser() {
        List<Card> cardList = new ArrayList<>();
        cardList.add(card);
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(cardRepository.findByUserId(user.getId())).thenReturn(cardList);
        when(cardMapper.entityToCardDTO(card)).thenReturn(cardDTO);
        doNothing().when(encryptionService).decryptCardData(any(CardDTO.class));

        List<CardDTO> cards = cardService.getCardsByUser("testUser");

        assertNotNull(cards);
        assertEquals(1, cards.size());
        assertEquals(cardDTO.getNumber(), cards.get(0).getNumber());
    }

    @Test
    void testGetAllCards() {
        List<Card> cardList = new ArrayList<>();
        cardList.add(card);
        when(cardRepository.findAll()).thenReturn(cardList);
        when(cardMapper.entityToCardDTO(card)).thenReturn(cardDTO);
        doNothing().when(encryptionService).decryptCardData(any(CardDTO.class));

        List<CardDTO> cards = cardService.getAllCards();

        assertNotNull(cards);
        assertEquals(1, cards.size());
        assertEquals(cardDTO.getNumber(), cards.get(0).getNumber());
    }

    @Test
    void testSearchCard_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(cardRepository.findByCardKeyAndUserId(cardDTO.getKey().toString(), user.getId())).thenReturn(card);
        when(cardMapper.entityToCardDTO(card)).thenReturn(cardDTO);
        doNothing().when(encryptionService).decryptCardData(any(CardDTO.class));

        CardDTO result = cardService.searchCard(cardDTO.getKey(), "testUser");

        assertNotNull(result);
        assertEquals(cardDTO.getKey(), result.getKey());
    }

    @Test
    void testSearchCard_CardNotFound() {
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(cardRepository.findByCardKeyAndUserId(cardDTO.getKey().toString(), user.getId())).thenReturn(null);

        assertThrows(CardNotFoundException.class, () -> cardService.searchCard(cardDTO.getKey(), "testUser"));
    }

    @Test
    void testUpdateCard_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(cardRepository.findByCardKeyAndUserId(cardDTO.getKey().toString(), user.getId())).thenReturn(card);
        when(encryptionService.encrypt(cardDTO.getNumber())).thenReturn("encrypted_1234567812345678");
        when(encryptionService.encrypt(cardDTO.getHolderName())).thenReturn("encrypted_Test Holder");
        when(encryptionService.encrypt(cardDTO.getCvv())).thenReturn("encrypted_123");
        when(cardRepository.save(any(Card.class))).thenReturn(card);

        cardService.updateCard(cardDTO, "testUser");

        verify(cardRepository).save(any(Card.class));
    }

    @Test
    void testUpdateCard_CardNotFound() {
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(cardRepository.findByCardKeyAndUserId(cardDTO.getKey().toString(), user.getId())).thenReturn(null);

        assertThrows(CardNotFoundException.class, () -> cardService.updateCard(cardDTO, "testUser"));
    }

    @Test
    void testDeleteCard_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(cardRepository.findByCardKeyAndUserId(cardDTO.getKey().toString(), user.getId())).thenReturn(card);
    }
}