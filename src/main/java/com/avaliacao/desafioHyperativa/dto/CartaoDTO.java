package com.avaliacao.desafioHyperativa.dto;

public class CartaoDTO {

    private Long id;
    private String numero;

    // Construtores, getters e setters
    public CartaoDTO() {
    }

    public CartaoDTO(Long id, String numero) {
        this.id = id;
        this.numero = numero;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}