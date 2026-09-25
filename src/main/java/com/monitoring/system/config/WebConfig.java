package com.monitoring.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${monitoring.frontend-url}")
    private String frontendUrl;

    @Override
    public void addCorsMappings(
            CorsRegistry registry) {

        registry.addMapping("/api/**")
                .allowedOrigins(frontendUrl.split(","))
                .allowedMethods(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE"
                )
                .allowedHeaders(
                        "Content-Type",
                        "X-API-Key"
                );
    }
}