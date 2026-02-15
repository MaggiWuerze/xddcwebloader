package de.maggiwuerze.xdccwebloader.config

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
class CorsConfig {

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val cfg = CorsConfiguration()
        cfg.setAllowedOrigins(List.of("http://localhost:3000")); // adjust to your UI dev server
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        cfg.setAllowCredentials(true); // only if you use cookies/credentials

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}
