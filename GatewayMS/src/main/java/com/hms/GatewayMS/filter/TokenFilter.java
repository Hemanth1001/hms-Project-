package com.hms.GatewayMS.filter;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenFilter extends AbstractGatewayFilterFactory<TokenFilter.Config> {

    private static final String SECRET = "8d0b723c98fc1fa5066805b2ac895f5d100a62efc8ca9343929fd3cbd5946161e113ae5f489586677a32c50216235d5cff0d3c8a63f7cd5b1c75e12d4903a050";        
    private static final SecretKey KEY =
            Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public TokenFilter() {
        super(Config.class);
    }

   @Override
    public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {


        System.out.println("===== TOKEN FILTER HIT =====");
System.out.println("Incoming request: "
        + exchange.getRequest().getMethod() + " "
        + exchange.getRequest().getURI());
System.out.println("Headers: " + exchange.getRequest().getHeaders());
System.out.println("============================");



        // ✅ Allow CORS preflight
        if (exchange.getRequest().getMethod() == org.springframework.http.HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }

        String path = exchange.getRequest().getPath().toString();

        // ✅ Public endpoints
        if (path.startsWith("/users/login") || path.startsWith("/users/register")) {
            return chain.filter(
                exchange.mutate()
                    .request(r -> r.header("X-Secret-Key", "SECRET"))
                    .build()
            );
        }

        HttpHeaders headers = exchange.getRequest().getHeaders();

        if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

            exchange = exchange.mutate()
                .request(r -> r.header("X-Secret-Key", "SECRET"))
                .build();

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    };
}


    public static class Config {

    }
}
