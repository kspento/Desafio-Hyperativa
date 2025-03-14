package com.avaliacao.desafioHyperativa.exception;

public class UnauthorizedCardAccessException extends RuntimeException {
    public UnauthorizedCardAccessException(String message) {
        super(message);
    }
}
