package com.example.gatewayasresourceserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) throws Exception {
        ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec jwt = http.authorizeExchange()
                .pathMatchers("/global/**", "/conditional/**").authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt();

        return http.build();
    }

}
