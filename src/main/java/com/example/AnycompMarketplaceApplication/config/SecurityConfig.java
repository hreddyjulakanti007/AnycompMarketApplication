package com.example.AnycompMarketplaceApplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()  // Allow Swagger UI
                                .anyRequest().authenticated()  // Require authentication for any other request
                )
                .csrf(csrf -> csrf.disable())  // Disable CSRF (use cautiously)
                .formLogin(withDefaults());  // Default login form (if needed)

        return http.build();  // Return the configured SecurityFilterChain
    }
}
