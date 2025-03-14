package com.avaliacao.desafioHyperativa.dto;

import java.time.LocalDate;

public class CardCreateDTO {

    private String number;
    private String holderName;
    private LocalDate expirationDate;
    private String cvv;

    public CardCreateDTO() {
    }

    public CardCreateDTO(Long id, String number, String holderName, LocalDate expirationDate, String cvv) {

        this.number = number;
        this.holderName = holderName;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}