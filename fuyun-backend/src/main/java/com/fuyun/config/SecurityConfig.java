package com.fuyun.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/auth/**").permitAll()
                        .pathMatchers("/h2-console/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/navigation/links/*/click").permitAll()
                        .anyExchange().permitAll()
                )
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .build();
    }
}