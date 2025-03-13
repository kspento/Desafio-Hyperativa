package com.avaliacao.desafioHyperativa.dto;

public class CartaoRequestDto {

    private String numero;

    // Construtores, getters e setters
    public CartaoRequestDto() {
    }

    public CartaoRequestDto(String numero) {
        this.numero = numero;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
