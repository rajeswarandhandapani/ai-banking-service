package com.rajeswarandhandapani.aibankingservice.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        try {
            if (authHeader != null && authHeader.toLowerCase().startsWith("bearer ")) {
                String token = authHeader.substring(7).trim();
                String username = extractUsername(token);
                if (username != null && !username.isEmpty()) {
                    ApplicationSecurityContext.setCurrentUser(username);
                    ApplicationSecurityContext.setJwt(authHeader.trim());
                }
            }
            filterChain.doFilter(request, response);
        } finally {
            // Ensure ThreadLocal cleanup for pooled threads
            ApplicationSecurityContext.clear();
        }
    }

    private String extractUsername(String jwt) {
        try {
            String[] parts = jwt.split("\\.");
            if (parts.length < 2)
                return null;

            String payloadJson = safeBase64UrlDecode(parts[1]);
            JsonNode claims = MAPPER.readTree(payloadJson);

            // Try common username claim keys (matches the provided JWT structure)
            String[] keys = new String[] {
                    "preferred_username", "username", "upn", "email", "name", "sub"
            };
            for (String key : keys) {
                JsonNode node = claims.get(key);
                if (node != null && !node.isNull() && !node.asText().isBlank()) {
                    return node.asText();
                }
            }
        } catch (Exception ignored) {
            LOGGER.warn("Failed to extract username from JWT: {}", jwt, ignored);
        }
        return null;
    }

    private String safeBase64UrlDecode(String part) {
        int pad = (4 - (part.length() % 4)) % 4;
        String padded = pad == 0 ? part : (part + "====".substring(0, pad));
        byte[] payloadBytes = Base64.getUrlDecoder().decode(padded);
        return new String(payloadBytes, StandardCharsets.UTF_8);
    }
}
