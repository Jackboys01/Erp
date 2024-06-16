package com.ERP.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
@EnableWebSecurity
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrfSpec -> csrfSpec.disable())  // Disable CSRF protection
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().permitAll()  // Permit all requests
                );
        return http.build();
    }
}
