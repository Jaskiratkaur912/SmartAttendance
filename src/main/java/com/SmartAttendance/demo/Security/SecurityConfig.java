package com.SmartAttendance.demo.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final OAuthSuccessHandler successHandler;
    private final JwtFilter jwtFilter;

    public SecurityConfig(OAuthSuccessHandler successHandler, JwtFilter jwtFilter) {
        this.successHandler = successHandler;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // ← change this
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/complete-registration").permitAll()
                        // ─── Teacher only endpoints ───
                        .requestMatchers("/api/class/create").hasRole("TEACHER")
                        // ─── Student only endpoints ───
                        .requestMatchers("/student/**").hasRole("STUDENT")
                        .requestMatchers("/api/users/delete").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .successHandler(successHandler)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}