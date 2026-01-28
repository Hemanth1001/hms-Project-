package com.hms.UserMS.jwt;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final Long JWT_TOKEN_VALIDITY = 5 * 60 * 60L;


    private static final String SECRET_KEY = "8d0b723c98fc1fa5066805b2ac895f5d100a62efc8ca9343929fd3cbd5946161e113ae5f489586677a32c50216235d5cff0d3c8a63f7cd5b1c75e12d4903a050";
     private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));


    public String generateToken(UserDetails userDetails) {    
        // Logic to generate JWT token using the email  
        Map<String, Object> claims = new HashMap<>();
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        claims.put("id", customUserDetails.getId());
        claims.put("email", customUserDetails.getEmail());
        claims.put("name", customUserDetails.getName());
        claims.put("role", customUserDetails.getRole());
        claims.put("profileId",customUserDetails.getProfileId());
        return doGenerateToken(claims, customUserDetails.getUsername());
    }

    public String doGenerateToken(Map<String, Object> claims, String subject) {
         return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, KEY).compact();
    }

}
