package com.avaliacao.desafioHyperativa.dto;

import java.util.UUID;

public class CardDTO {

    private UUID key;

    private String Number;

    public UUID getKey() {
        return key;
    }

    public void setKey(UUID key) {
        this.key = key;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }
}
