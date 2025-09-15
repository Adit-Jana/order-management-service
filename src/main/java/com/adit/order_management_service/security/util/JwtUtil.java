package com.adit.order_management_service.security.util;

import com.auth0.jwt.JWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = "my-super-duper-secret-key-for-oms";

    private static final long EXPIRATION_TIME = 3600000;

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    private static final Key key = new SecretKeySpec(SECRET_KEY.getBytes(),
            SIGNATURE_ALGORITHM.getJcaName());

    // generate jst token based on username and role
    public static String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SIGNATURE_ALGORITHM, key)
                .compact();
    }

    // validate token
    public static boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJwt(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public static String getRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class);
    }
}
