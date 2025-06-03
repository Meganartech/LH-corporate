package com.knowledgeVista.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import javax.crypto.SecretKey;

@Configuration
public class SecretConfig {
    
    @Bean
    public String jwtSecret() {
        // Generate a secure random JWT secret if not provided
        String envSecret = System.getenv("JWT_SECRET");
        if (envSecret == null || envSecret.trim().isEmpty()) {
            // Generate a secure key specifically for HS256
            SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            return Base64.getEncoder().encodeToString(key.getEncoded());
        }
        // If environment variable is provided, ensure it's base64 encoded and has sufficient length
        try {
            byte[] decodedKey = Base64.getDecoder().decode(envSecret);
            if (decodedKey.length * 8 < 256) { // Check if key is at least 256 bits
                // If key is too short, generate a new one
                SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
                return Base64.getEncoder().encodeToString(key.getEncoded());
            }
            return envSecret;
        } catch (IllegalArgumentException e) {
            // If not base64 encoded or invalid, generate a new secure key
            SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            return Base64.getEncoder().encodeToString(key.getEncoded());
        }
    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 