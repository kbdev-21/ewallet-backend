package com.example.bank_midterm.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {
    private static final String SECRET = "Pq+r7sxSrFri47FcFjRritMaFYAO3/v8wFjK9p1T64YVebHFEASX8B0LqzutNFpliMmcsDiqLdcMPiK/xZzxSQ==";

    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        int durationInMinutes = 60;
        return Jwts.builder()
            .subject(userDetails.getUsername())
            .expiration(new Date(now.getTime() + durationInMinutes * 60 * 1000))
            .signWith(getSignKey())
            .compact();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token) {
        var expiration = extractExpiration(token);
        return expiration.after(new Date());
    }

    public String extractSubject(String token) {
        return Jwts.parser()
            .verifyWith(getSignKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    public Date extractExpiration(String token) {
        return Jwts.parser()
            .verifyWith(getSignKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getExpiration();
    }
}
