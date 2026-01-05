package com.banking.gateway.config;

import com.banking.gateway.filter.AuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final AuthenticationFilter authenticationFilter;

    public GatewayConfig(AuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("account-service", r -> r
                .path("/api/v1/accounts/**", "/api/v1/customers/**", "/api/v1/transfers/**")
                .filters(f -> f
                    .filter(authenticationFilter.apply(new AuthenticationFilter.Config()))
                    .addRequestHeader("X-Gateway", "API-Gateway")
                    .circuitBreaker(config -> config
                        .setName("accountServiceCircuitBreaker")
                        .setFallbackUri("forward:/fallback/accounts")))
                .uri("lb://account-service"))
            .route("transaction-service", r -> r
                .path("/api/v1/transactions/**")
                .filters(f -> f
                    .filter(authenticationFilter.apply(new AuthenticationFilter.Config()))
                    .addRequestHeader("X-Gateway", "API-Gateway"))
                .uri("lb://transaction-service"))
            .build();
    }
}

