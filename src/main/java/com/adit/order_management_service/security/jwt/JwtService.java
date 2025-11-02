package com.adit.order_management_service.security.jwt;

import com.adit.order_management_service.entity.RefreshToken;
import com.adit.order_management_service.repo.RefreshTokenRepo;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Date;
import java.time.Instant;
import java.util.List;

@Component
public class JwtService {

    private final Key key;
    private final String issuer;
    private final long accessExpSeconds;
    private final long refreshExpSeconds;
    private final RefreshTokenRepo refreshTokenRepo;

    @Autowired
    public JwtService(@Value("${security.jwt.secret}") String secret,
                      @Value("${security.jwt.issuer}") String issuer,
                      @Value("${security.jwt.access-token-exp-seconds}") long accessExpSeconds,
                      @Value("${security.jwt.refresh-token-exp-seconds}") long refreshExpSeconds, RefreshTokenRepo refreshTokenRepo){
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.issuer = issuer;
        this.accessExpSeconds = accessExpSeconds;
        this.refreshExpSeconds = refreshExpSeconds;
        this.refreshTokenRepo = refreshTokenRepo;
    }

    // generate access token
    public String generateAccessToken(UserDetails user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(user.getUsername())
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(accessExpSeconds)))
                .claim("roles", user.getAuthorities().stream()
                        .map(a -> a.getAuthority())
                        .toList())
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(org.springframework.security.core.userdetails.UserDetails user) {
        Instant now = Instant.now();
        String token = Jwts.builder()
                .subject(user.getUsername())
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(refreshExpSeconds)))
                .claim("type", "refresh")
                .signWith(key)
                .compact();

        // update as we need refresh token regenerate every time
        RefreshToken entity = new RefreshToken();
        entity.setToken(token);
        entity.setUsername(user.getUsername());
        entity.setExpiry(now.plusSeconds(refreshExpSeconds));
        entity.setRevoked(false);
        refreshTokenRepo.save(entity);

        return token;
    }

    public boolean isValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key)
                    .requireIssuer(issuer)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .requireIssuer(issuer)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        var claims = Jwts.parser()
                .verifyWith((SecretKey) key)
                .requireIssuer(issuer)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Object rolesObj = claims.get("roles");
        if (rolesObj instanceof List<?> list) {
            return list.stream().map(Object::toString).toList();
        }
        return List.of();
    }

    public boolean isRefreshToken(String token) {
        var claims = Jwts.parser()
                .verifyWith((SecretKey) key)
                .requireIssuer(issuer)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return "refresh".equals(claims.get("type"));
    }

}

