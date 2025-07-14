package com.busticket.user_service.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;

    @Value("${jwt.expiration.customer}")
    private long customerExpiration;

    @Value("${jwt.expiration.admin}")
    private long adminExpiration;

    @Value("${jwt.expiration.bus_operator}")
    private long busOperatorExpiration;

    @Autowired
    public JwtTokenProvider(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @PostConstruct
    public void validateExpirations() {
        if (customerExpiration <= 0 || adminExpiration <= 0 || busOperatorExpiration <= 0) {
            throw new IllegalStateException("JWT expiration times must be positive");
        }
    }

    public String generateToken(UserDetails userDetails) {
        long expiration = determineExpiration(userDetails);
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("authorities", userDetails.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private long determineExpiration(UserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities().stream()
                .map(Object::toString)
                .toList();
        if (roles.contains("ROLE_ADMIN")) {
            return adminExpiration;
        } else if (roles.contains("ROLE_BUS_OPERATOR")) {
            return busOperatorExpiration;
        } else {
            return customerExpiration; // Default to customer expiration
        }
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }
}
