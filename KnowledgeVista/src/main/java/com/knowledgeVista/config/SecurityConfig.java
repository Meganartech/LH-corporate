package com.knowledgeVista.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.Customizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    
    @Value("${spring.mail.password:}")
    private String mailPassword;
    
    @Value("${spring.mail.username:}")
    private String mailUsername;
    
    private final BCryptPasswordEncoder passwordEncoder;
    
    public SecurityConfig(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        logger.debug("Configuring security filter chain");
        
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> {
                logger.debug("Configuring authorization rules");
                auth.anyRequest().permitAll(); // Temporarily allow all requests
            })
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));
        
        logger.debug("Security configuration completed");
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        logger.debug("Configuring CORS");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://lhdemo.vsmartengine.com"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "Origin", "X-Requested-With"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        logger.debug("CORS configuration completed");
        return source;
    }
    
    // Method to mask sensitive data before sending to frontend
    public String maskSensitiveData(String data, String type) {
        if (data == null || data.isEmpty()) {
            return "••••••••";
        }
        
        switch (type) {
            case "email":
                String[] parts = data.split("@");
                return parts[0].substring(0, Math.min(3, parts[0].length())) + "...@" + parts[1];
            case "password":
                return "••••••••";
            case "host":
                String[] hostParts = data.split("\\.");
                return hostParts[0] + "...";
            case "port":
                return "••••";
            default:
                return data.substring(0, Math.min(3, data.length())) + "...";
        }
    }
    
    // Method to validate sensitive data
    public boolean validateSensitiveData(String data, String type) {
        if (data == null || data.isEmpty()) {
            return false;
        }
        
        switch (type) {
            case "email":
                return data.matches("^[A-Za-z0-9+_.-]+@(.+)$");
            case "password":
                return data.length() >= 8;
            case "host":
                return data.matches("^[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
            case "port":
                try {
                    int port = Integer.parseInt(data);
                    return port > 0 && port < 65536;
                } catch (NumberFormatException e) {
                    return false;
                }
            default:
                return true;
        }
    }
} 