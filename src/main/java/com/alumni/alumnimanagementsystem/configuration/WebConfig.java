package com.alumni.alumnimanagementsystem.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// SRS 2.4  — Allow React frontend (port 3000) to call backend
// SRS 2.5  — All API endpoints under /api/v1
// NFR 3.3.1 — Controlled CORS access
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                // SRS 2.4 — React frontend URL
                .allowedOrigins(
                    "http://localhost:3000",
                    "http://localhost:3001"
                )
                // Allow all standard HTTP methods
                .allowedMethods(
                    "GET",
                    "POST",
                    "PUT",
                    "DELETE",
                    "OPTIONS"
                )
                // Allow all headers including Authorization (JWT)
                .allowedHeaders("*")
                // NFR 3.3.1 — Allow credentials (JWT token)
                .allowCredentials(true)
                // Cache preflight for 1 hour
                .maxAge(3600);
    }
}