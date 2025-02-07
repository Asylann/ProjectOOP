package com.sportwearshop.sportwearwebshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/products/**").hasAnyRole("ADMIN", "MANAGER", "EDITOR")
                        .requestMatchers("/api/categories/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/api/orders/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/api/order-items/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/api/user/**").hasRole("ADMIN")
                        .requestMatchers("/db-config").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(basic->basic.init(http));
        return http.build();
    }

    @SuppressWarnings("deprecation")
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}