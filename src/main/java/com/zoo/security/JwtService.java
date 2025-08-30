package com.zoo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey; // önemli: SecretKey kullan
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    private final SecretKey key;
    private final long expirationMs;

    public JwtService(@Value("${app.jwt.secret:}") String secret,
                      @Value("${app.jwt.expiration-ms:3600000}") long expirationMs) {
        byte[] bytes = (secret== null ? "" : secret).getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            secret = "dev-only-fallback-secret-please-change-this-0123456789-abcdef";
            bytes = secret.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        }
        this.key = io.jsonwebtoken.security.Keys.hmacShaKeyFor(bytes);
        this.expirationMs = expirationMs;
    }

    public String generate(String username, Map<String, Object> claims) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)                                   // 0.12 API
                .claims(claims)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expirationMs)))
                .signWith(key)                                       // alg key'den seçilir (HS256)
                .compact();
    }

    public Claims parse(String token) {
        return Jwts.parser()                                     // 0.12 API (parserBuilder değil)
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

