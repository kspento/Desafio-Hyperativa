package com.avaliacao.desafioHyperativa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncryptionConfig {

    @Value("${encryption.secret-key}")
    private String secretKey;

    @Value("${encryption.iv}")
    private String iv;

    public String getSecretKey() {
        return secretKey;
    }

    public String getIv() {
        return iv;
    }
}