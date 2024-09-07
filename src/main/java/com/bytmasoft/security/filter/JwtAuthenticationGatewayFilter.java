package com.bytmasoft.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationGatewayFilter implements GatewayFilter {

    @Value("${jwt.secret.key}")
    private String secret;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Check if the Authorization header is present
        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            return chain.filter(exchange);  // No token, forward the request
        }

        String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);  // Remove "Bearer " prefix
            try {
                // Validate the JWT token
                Claims claims = Jwts.parser().setSigningKey(secret).build().parseClaimsJws(token).getBody();
                // Add the username to the request headers for downstream services
                ServerHttpRequest modifiedRequest = request.mutate()
                        .header("username", claims.getSubject())
                        .build();

                // Forward the request with the modified headers
                return chain.filter(exchange.mutate().request(modifiedRequest).build());
            } catch (Exception e) {
                // Handle invalid JWT token case
                return Mono.error(new RuntimeException("Invalid JWT Token"));
            }
        }

        return chain.filter(exchange);  // Forward the request if token is valid
    }

}