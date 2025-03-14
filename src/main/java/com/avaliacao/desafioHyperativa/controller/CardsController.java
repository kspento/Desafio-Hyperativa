package com.avaliacao.desafioHyperativa.controller;

import com.avaliacao.desafioHyperativa.dto.CardCreateDTO;
import com.avaliacao.desafioHyperativa.dto.CardDTO;
import com.avaliacao.desafioHyperativa.model.Card;
import com.avaliacao.desafioHyperativa.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/cards")
public class CardsController {

    private final CardService cardService;

    public CardsController(CardService cardService) {
        this.cardService = cardService;
    }

    @Operation(summary = "Retrieve all cards associated with the authenticated user",
            description = "This endpoint returns a list of cards that belong to the authenticated user.")
    @GetMapping("/my-cards")
    public ResponseEntity<List<CardDTO>> retrieveMyCards() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<CardDTO> cards = cardService.getCardsByUser(username);
        return ResponseEntity.ok(cards);
    }

    @Operation(summary = "Retrieve all cards in the system",
            description = "This endpoint is restricted to admin users only. Returns a list of all cards in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all cards"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin access required")
    })
    @GetMapping("/get-all")
    public ResponseEntity<List<CardDTO>> listAllCards() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<CardDTO> cards = cardService.getAllCards();
        return ResponseEntity.ok(cards);
    }

    @Operation(summary = "Create a new card",
            description = "This endpoint creates a new card for the authenticated user based on the provided card number.")
    @ApiResponse(responseCode = "201", description = "Successfully created the card")
    @PostMapping
    public HttpStatus createCard(@RequestBody String numeroCartao) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        cardService.registerCard(numeroCartao, authentication.getName());
        return HttpStatus.CREATED;
    }

    @Operation(summary = "Search for a card by its unique identifier",
            description = "This endpoint allows users to search for a specific card by its unique key.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the card"),
            @ApiResponse(responseCode = "404", description = "Card not found")
    })
    @GetMapping("/{key}")
    public ResponseEntity<CardDTO> searchCard(@PathVariable UUID key) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CardDTO card = cardService.searchCard(key, authentication.getName());
        if (card == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(card);
    }

    @Operation(summary = "Update an existing card",
            description = "This endpoint allows the authenticated user to update the details of an existing card.")
    @ApiResponse(responseCode = "200", description = "Successfully updated the card")
    @PutMapping
    public HttpStatus update(@RequestBody CardDTO cardCreateDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CardDTO card = cardService.updateCard(cardCreateDTO, authentication.getName());

        return HttpStatus.OK;
    }

    @Operation(summary = "Delete a card by its unique identifier",
            description = "This endpoint deletes a card for the authenticated user. The card is identified by its unique key.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the card"),
            @ApiResponse(responseCode = "404", description = "Card not found")
    })
    @DeleteMapping("/{key}")
    public HttpStatus deleteCard(@PathVariable UUID key) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!cardService.deleteCard(key, authentication.getName())) {
            return HttpStatus.NOT_FOUND;
        }
        return HttpStatus.OK;
    }
}