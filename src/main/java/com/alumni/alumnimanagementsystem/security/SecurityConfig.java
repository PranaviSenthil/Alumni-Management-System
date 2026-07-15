package com.alumni.alumnimanagementsystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// FR1.4 — configures which routes need JWT authentication
// NFR 3.3.1 — stateless security, no sessions, JWT only
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CORS is already handled by WebConfig; disable CSRF
            // since this is a stateless JWT-based REST API
            .csrf(csrf -> csrf.disable())

            // NFR 3.3.1 — no server-side sessions, JWT only
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

.authorizeHttpRequests(auth -> auth
    // FR1.1 — registration stays public
    .requestMatchers("/api/v1/auth/register").permitAll()

    // FR1.4 — login stays public (no token yet at login)
    .requestMatchers("/api/v1/auth/login").permitAll()

    // FR15.1, FR15.3 — forgot/reset password stay public
    .requestMatchers("/api/v1/auth/forgot-password").permitAll()
    .requestMatchers("/api/v1/auth/validate-reset-token").permitAll()
    .requestMatchers("/api/v1/auth/reset-password").permitAll()

    // Swagger UI + OpenAPI docs — public so you can explore/test the API
    // (dev convenience only; not an SRS requirement)
    .requestMatchers(
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/v3/api-docs/**"
    ).permitAll()

    // everything else requires a valid JWT
    .anyRequest().authenticated()
)
            // FR1.4 — plug in our JWT filter before Spring's own
            // username/password filter runs
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}