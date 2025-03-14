package com.avaliacao.desafioHyperativa.service;

import com.avaliacao.desafioHyperativa.config.EncryptionConfig;
import com.avaliacao.desafioHyperativa.dto.CardDTO;
import com.avaliacao.desafioHyperativa.exception.DecryptionException;
import com.avaliacao.desafioHyperativa.exception.EncryptionException;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class EncryptionService {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    private final EncryptionConfig config;

    public EncryptionService(EncryptionConfig config) {
        this.config = config;
    }

    public String decrypt(String encryptedValue) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(config.getIv().getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKeySpec = new SecretKeySpec(config.getSecretKey().getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            byte[] decryptedValue = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
            return new String(decryptedValue);
        } catch (Exception e) {
            throw new DecryptionException("Error decrypting data", e);
        }
    }

    public String encrypt(String value) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(config.getIv().getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKeySpec = new SecretKeySpec(config.getSecretKey().getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            byte[] encryptedValue = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encryptedValue);
        } catch (Exception e) {
            throw new EncryptionException("Error encrypting data", e);
        }
    }

    public void decryptCardData(CardDTO cardDTO) {
        cardDTO.setNumber(decrypt(cardDTO.getNumber()));
        cardDTO.setHolderName(decrypt(cardDTO.getHolderName()));
        cardDTO.setCvv(decrypt(cardDTO.getCvv()));
    }
}