package org.example.marathon.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class JwtUtils {

    private static final SecretKey signKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Same key for both signing and parsing

    // Generation of JWT token
    public static String generateJwt(Map<String, Object> claims) {
        String jwt = Jwts.builder()
                .addClaims(claims)
                .signWith(signKey, SignatureAlgorithm.HS256)
                .setExpiration(new Date(System.currentTimeMillis() + 43200000L)) // 12 hours expiration time
                .compact();
        System.out.println("Generated JWT: " + jwt); // Log the generated token
        return jwt;
    }

    // Parsing of JWT token
    public static Claims parseJWT(String jwt) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(signKey)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            return claims;
        } catch (Exception e) {
            System.err.println("JWT parsing error: " + e.getMessage());
            throw e;  // Rethrow or handle the error as needed
        }
    }
}