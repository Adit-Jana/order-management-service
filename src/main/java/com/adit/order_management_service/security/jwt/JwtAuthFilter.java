package com.adit.order_management_service.security.jwt;

import com.adit.order_management_service.entity.AccessToken;
import com.adit.order_management_service.repo.AccessTokenRepo;
import com.adit.order_management_service.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;


@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final AccessTokenRepo accessTokenRepo;

    @Autowired
    public JwtAuthFilter(JwtService jwtService, CustomUserDetailsService userDetailsService, AccessTokenRepo accessTokenRepo) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.accessTokenRepo = accessTokenRepo;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String token;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        token = authHeader.substring(7).trim();

        // ✅ Skip access token validation for /auth/refresh
        String requestPath = request.getRequestURI();
        if (requestPath.contains("/auth/refresh")) {
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ Validate token structure and expiration
        if (!jwtService.isValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ Enforce token type: must be "access"
        Claims claims = jwtService.extractAllClaims(token);
        String type = claims.get("type", String.class);
        if (!"access".equals(type)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token type for authentication");
            return;
        }

        // ✅ Check token revocation status in DB
        AccessToken tokenEntity = accessTokenRepo.findByToken(token)
                .filter(t -> !t.isRevoked() && t.getExpiry().isAfter(Instant.now()))
                .orElseThrow(() -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    try {
                        response.getWriter().write("Access token is revoked or expired");
                    } catch (IOException ignored) {}
                    return new JwtException("Access token is revoked or expired");
                });

        // ✅ Authenticate user
        String username = jwtService.extractUsername(token);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);

    }
}
