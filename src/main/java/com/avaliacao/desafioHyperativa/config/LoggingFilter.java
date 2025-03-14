package com.avaliacao.desafioHyperativa.config;

import com.avaliacao.desafioHyperativa.model.Log;
import com.avaliacao.desafioHyperativa.repository.LogRepository;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // Ignorar logs para o Swagger
        if (requestURI.contains("/swagger-ui/") || requestURI.contains("/v3/api-docs/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);

        long startTime = System.currentTimeMillis();
        try {
            ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
            ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
            filterChain.doFilter(wrappedRequest, wrappedResponse);
            logRequest(wrappedRequest);
            logResponse(wrappedResponse);
            wrappedResponse.copyBodyToResponse();
        } finally {
            long endTime = System.currentTimeMillis();
            logger.info("Response: {} - {}ms", response.getStatus(), endTime - startTime);
            MDC.remove("requestId");
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) throws IOException {
        logger.info("Request: {} {} - Query Params: {}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            String requestBody = new String(content, request.getCharacterEncoding());
            logger.info("Request Body: {}", requestBody);
        }
    }

    private void logResponse(ContentCachingResponseWrapper response) throws IOException {
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            String responseBody = new String(content, response.getCharacterEncoding());
            logger.info("Response Body: {}", responseBody);
        }
    }
}
