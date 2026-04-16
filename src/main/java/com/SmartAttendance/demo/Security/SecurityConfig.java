package com.SmartAttendance.demo.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                .authorizeHttpRequests(auth -> auth

                        // OAuth URLs
                        .requestMatchers(
                                "/oauth2/**",
                                "/login/oauth2/**",
                                "/error"
                        ).permitAll()

                        // Public APIs
                        .requestMatchers(
                                "/api/users/complete-registration",
                                "/api/users/delete"
                        ).permitAll()

                        // Teacher
                        .requestMatchers("/api/class/create")
                        .hasRole("TEACHER")

                        // Student
                        .requestMatchers("/student/**")
                        .hasRole("STUDENT")

                        // everything else
                        .anyRequest().authenticated()
                )

                .oauth2Login(oauth -> oauth
                        .successHandler(successHandler)
                )

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}