package com.knowledgeVista.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("file:.env")
public class EnvConfig {
    // Configuration class to load .env file
} 