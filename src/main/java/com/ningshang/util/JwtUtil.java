package com.ningshang.util;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private final java.util.concurrent.ConcurrentHashMap<String, Long> revoked = new java.util.concurrent.ConcurrentHashMap<>();

    @Value("${jwt.secret:ningshang-secret-key-2026}")
    private String secret;

    @Value("${jwt.expire:86400000}")
    private Long expire;

    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("role", role);
        return generateToken(username, role, null);
    }

    public String generateToken(String username, String role, Long groupId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("role", role);
        if (groupId != null) {
            claims.put("groupId", groupId);
        }
        return Jwts.builder()
                .setClaims(claims)
                .setId(java.util.UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token) {
        try {
            revoked.entrySet().removeIf(entry -> entry.getValue() <= System.currentTimeMillis());
            if (revoked.containsKey(token)) return false;
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void revokeToken(String token) {
        Claims claims = parseToken(token);
        revoked.put(token, claims.getExpiration().getTime());
    }

    public String getUsername(String token) {
        return parseToken(token).get("username", String.class);
    }

    public String getRole(String token) {
        return parseToken(token).get("role", String.class);
    }

    public Long getGroupId(String token) {
        Object val = parseToken(token).get("groupId");
        if (val instanceof Number) return ((Number) val).longValue();
        return null;
    }
}
