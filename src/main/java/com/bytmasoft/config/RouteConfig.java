package com.bytmasoft.config;

import com.bytmasoft.security.filter.JwtAuthenticationGatewayFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Value("${spring.cloud.gateway.routes[0].id}")
    private String student_service_id;
    @Value("${spring.cloud.gateway.routes[0].predicates[0]}")
    private String student_service_path;
    @Value("${spring.cloud.gateway.routes[0].uri}")
    private String student_service_uri;



    private final JwtAuthenticationGatewayFilter jwtAuthenticationFilter;

    public RouteConfig(JwtAuthenticationGatewayFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(student_service_id, r -> r.path(student_service_path)
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri(student_service_uri))  // Student Service
                .build();
    }
}