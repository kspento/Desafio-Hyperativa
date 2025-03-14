package com.avaliacao.desafioHyperativa.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "card")
public class Card {

    public Card() {
        this.cardKey = UUID.randomUUID().toString();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private String number;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "card_key", unique = true)
    private String cardKey;

    // Getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public UUID getCardKey() {
        return cardKey != null ? UUID.fromString(cardKey) : null;
    }

    public void setCardKey(UUID key) {
        this.cardKey = key != null ? key.toString() : null;
    }
}