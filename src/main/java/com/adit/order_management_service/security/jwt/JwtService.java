package com.adit.order_management_service.security.jwt;

import com.adit.order_management_service.entity.AccessToken;
import com.adit.order_management_service.entity.RefreshToken;
import com.adit.order_management_service.repo.AccessTokenRepo;
import com.adit.order_management_service.repo.RefreshTokenRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;
import java.util.List;

@Component
public class JwtService {

    private final String issuer;
    private final long accessExpSeconds;
    private final long refreshExpSeconds;
    private final RefreshTokenRepo refreshTokenRepo;
    private final AccessTokenRepo accessTokenRepo;

    @Value("${security.jwt.secret}")
    private String secret;

    @Autowired
    public JwtService(
            @Value("${security.jwt.issuer}") String issuer,
            @Value("${security.jwt.access-token-exp-seconds}") long accessExpSeconds,
            @Value("${security.jwt.refresh-token-exp-seconds}") long refreshExpSeconds, RefreshTokenRepo refreshTokenRepo, AccessTokenRepo accessTokenRepo) {
        this.issuer = issuer;
        this.accessExpSeconds = accessExpSeconds;
        this.refreshExpSeconds = refreshExpSeconds;
        this.refreshTokenRepo = refreshTokenRepo;
        this.accessTokenRepo = accessTokenRepo;
    }

    public SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }


    // generate access token
    public String generateAccessToken(UserDetails user) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(accessExpSeconds);

        String token = Jwts.builder()
                .subject(user.getUsername())
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(accessExpSeconds)))
                .claim("roles", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .claim("type", "access")
                .signWith(getSigningKey())
                .compact();

        AccessToken entity = new AccessToken();
        entity.setToken(token);
        entity.setUsername(user.getUsername());
        entity.setExpiry(expiry);
        entity.setRevoked(false);
        accessTokenRepo.save(entity);

        return token;
    }

    public String generateRefreshToken(UserDetails user) {
        Instant now = Instant.now();
        System.out.println("now time = " + now);
        System.out.println("refreshExpSeconds = " + refreshExpSeconds);
        Instant expiry = now.plusSeconds(refreshExpSeconds);


        String token = Jwts.builder()
                .subject(user.getUsername())
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .claim("type", "refresh")
                .signWith(getSigningKey())
                .compact();

        // update as we need refresh token regenerate every time
        RefreshToken entity = new RefreshToken();
        entity.setToken(token);
        entity.setUsername(user.getUsername());
        entity.setExpiry(expiry);
        entity.setRevoked(false);
        refreshTokenRepo.save(entity);

        return token;
    }

    public boolean isValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
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
                .verifyWith(getSigningKey())
                .requireIssuer(issuer)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        var claims = Jwts.parser()
                .verifyWith(getSigningKey())
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
                .verifyWith(getSigningKey())
                .requireIssuer(issuer)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return "refresh".equals(claims.get("type"));
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && isValid(token);
    }


}

