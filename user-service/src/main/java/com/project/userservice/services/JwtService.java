package com.project.userservice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * Service class responsible for generating, validating, and extracting JWT tokens.
 */
@Service
public class JwtService {

    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    @Value("${jwt.expiration.time}")
    private long tokenExpiration;

    /**
     * Extract the subject (typically user's email) from the JWT token.
     */
    public String extractSubject(String token) {
        return extraClaim(token, Claims::getSubject);
    }

    /**
     * Extract a specific claim using the provided claims resolver function.
     */
    public<T> T extraClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generate a JWT token with the provided extra claims and subject.
     */
    public String generateToken(
            Map<String, Object> extraClaims,
            String subject
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Check if a given token is valid for the provided subject.
     */
    public boolean isTokenValid(String token, String subject) {
        final String tokenSubject = extractSubject(token);
        return (subject.equals(tokenSubject)) && !isTokenExpired(token);
    }

    /**
     * Check if a given token has expired.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extract the expiration date from a token's claims.
     */
    private Date extractExpiration(String token) {
        return extraClaim(token, Claims::getExpiration);
    }

    /**
     * Extract all claims from the token using the signing key.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    /**
     * Retrieve the signing key for JWT token creation and validation.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
