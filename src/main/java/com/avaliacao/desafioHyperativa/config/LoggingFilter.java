package com.avaliacao.desafioHyperativa.config;

import com.avaliacao.desafioHyperativa.model.Log;
import com.avaliacao.desafioHyperativa.repository.LogRepository;
import com.avaliacao.desafioHyperativa.service.LogService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import java.time.LocalDateTime;
import java.util.UUID;
import org.slf4j.MDC;


@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Autowired
    private LogService logService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        if (requestURI.contains("/swagger-ui/") || requestURI.contains("/v3/api-docs/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);

        long startTime = System.currentTimeMillis();
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
            logRequest(wrappedRequest, requestId); // Passa o requestId
            logResponse(wrappedResponse, requestId, requestURI); // Passa o requestId
            wrappedResponse.copyBodyToResponse();
        } finally {
            long endTime = System.currentTimeMillis();
            logger.info("Response: {} - {}ms", response.getStatus(), endTime - startTime);
            MDC.remove("requestId");
        }
    }

    private void logRequest(ContentCachingRequestWrapper request, String requestId) throws IOException {
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();
        String method = request.getMethod();
        String requestBody = "";
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            requestBody = new String(content, request.getCharacterEncoding());
        }

        logService.log("INFO", "Request: " + method + " " + requestURI + " - Query Params: " + queryString, requestBody, queryString, null, requestURI);
    }

    private void logResponse(ContentCachingResponseWrapper response, String requestId, String requestURI) throws IOException {
        String responseBody = "";
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            responseBody = new String(content, response.getCharacterEncoding());
        }

        logService.log("INFO", "Response: " + response.getStatus() + " - URI: " + requestURI, null, null, responseBody,requestURI); // Inclui a rota
    }
}
