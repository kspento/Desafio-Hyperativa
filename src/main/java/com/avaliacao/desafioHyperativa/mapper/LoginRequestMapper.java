package com.avaliacao.desafioHyperativa.mapper;

import com.avaliacao.desafioHyperativa.dto.LoginRequestDTO;
import com.avaliacao.desafioHyperativa.model.LoginRequest;
import org.springframework.stereotype.Component;

@Component
public class LoginRequestMapper {

    public LoginRequestDTO toDTO(LoginRequest loginRequest) {
        if (loginRequest == null) {
            return null;
        }
        return new LoginRequestDTO(loginRequest.getUsername(), loginRequest.getPassword());
    }

    public LoginRequest toEntity(LoginRequestDTO loginRequestDTO) {
        if (loginRequestDTO == null) {
            return null;
        }
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(loginRequestDTO.getUsername());
        loginRequest.setPassword(loginRequestDTO.getPassword());
        return loginRequest;
    }
}