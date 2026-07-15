package com.alumni.alumnimanagementsystem.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.annotation.Nonnull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// FR1.4 — runs once per request, checks for valid JWT
// before allowing access to protected endpoints
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
        protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // FR1.4 — token must be sent as "Bearer <token>"
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // FR1.4 — validate signature and expiry
            if (jwtUtil.validateToken(token)) {
                String email    = jwtUtil.extractEmail(token);
                String userType = jwtUtil.extractUserType(token);

                // FR1.4 — set authenticated user in security context
                // so controllers know who is making the request
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + userType))
                        );
                SecurityContextHolder.getContext()
                        .setAuthentication(authToken);
            }
        }

        // continue to the next filter/controller regardless
        // (unauthenticated requests are blocked later by SecurityConfig
        //  for protected routes; public routes like /register stay open)
        filterChain.doFilter(request, response);
    }
}