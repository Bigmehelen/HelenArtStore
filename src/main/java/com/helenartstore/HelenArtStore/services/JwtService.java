package com.helenartstore.HelenArtStore.services;

import io.jsonwebtoken.Claims;
import com.helenartstore.HelenArtStore.config.JwtConfigProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtService {
    @Autowired
    private JwtConfigProperties jwtConfig;

    @PostConstruct
    public void debugJwtSecret() {
        System.out.println("JWT SECRET LENGTH = " + jwtConfig.getSecret().length());
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())        // ✅ Use this
                .build()
                .parseSignedClaims(token)        // ✅ Use this
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        return createToken(extraClaims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)              // ✅ NEW API
                .subject(subject)            // ✅ NEW API
                .issuedAt(new Date(System.currentTimeMillis()))     // ✅ NEW API
                .expiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))  // ✅ NEW API
                .signWith(getSignKey())      // ✅ NEW API (no algorithm needed)
                .compact();
    }

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(
                jwtConfig.getSecret().getBytes(java.nio.charset.StandardCharsets.UTF_8)
        );
    }

}
