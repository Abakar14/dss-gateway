package com.bytmasoft.security;


/*import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {

    private final JwtAuthenticationGatewayFilter jwtAuthenticationGatewayFilter;

    public GatewaySecurityConfig(JwtAuthenticationGatewayFilter jwtAuthenticationGatewayFilter) {
        this.jwtAuthenticationGatewayFilter = jwtAuthenticationGatewayFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable())  // Disable CSRF for APIs
                .authorizeExchange(exchanges -> {
                    exchanges
                            .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // Allow preflight requests
                            .pathMatchers("/auth/**", "/public/**").permitAll()  // Public routes
                            .anyExchange().authenticated();  // Secure all other routes
                })
                .addFilterAt(jwtAuthenticationGatewayFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

}*/
