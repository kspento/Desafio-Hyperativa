package com.avaliacao.desafioHyperativa.service;

import com.avaliacao.desafioHyperativa.model.Log;
import com.avaliacao.desafioHyperativa.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository; // Injeção do LogRepository

    public void log(String level, String message, String requestBody, String urlParams, String responseBody, String route) {
        Log log = new Log();
        log.setTimestamp(LocalDateTime.now());
        log.setLevel(level);
        log.setMessage(message);
        log.setRoute(route);
        log.setRequestBody(requestBody);
        log.setUrlParams(urlParams);
        log.setResponseBody(responseBody);
        logRepository.save(log);
    }
}