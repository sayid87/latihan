package com.latihan.latihan.configs;

import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.authorizeHttpRequests().anyRequest().permitAll();
        return http.build();
    }
}
