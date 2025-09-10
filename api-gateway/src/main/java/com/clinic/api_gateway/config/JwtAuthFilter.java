package com.clinic.api_gateway.config;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements GlobalFilter {

    private final WebClient webClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();

        // Skip auth for public endpoints
        if (path.startsWith("/auth/login") || path.startsWith("/auth/register")) {
            return chain.filter(exchange);
        }

        // Get token from header
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange.getResponse());
        }

        String token = authHeader.substring(7);

        // Validate token by calling Auth Service
        return webClient.post()
                .uri("http://auth-service:8080/auth/validate")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(AuthValidationResponse.class)
                .flatMap(response -> {
                    if (response != null && response.isValid()) {
                        // Add user info to headers for downstream services
                        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                                .header("X-User-Id", response.getUserId())
                                .header("X-User-Name", response.getUsername())
                                .header("X-User-Roles", response.getRole())
                                .build();

                        return chain.filter(exchange.mutate().request(modifiedRequest).build());
                    } else {
                        return unauthorized(exchange.getResponse());
                    }
                })
                .onErrorResume(e -> {
                    return unauthorized(exchange.getResponse());
                });
    }

    private Mono<Void> unauthorized(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }
}

// DTO for Auth Service response
@Data
class AuthValidationResponse {
    private boolean valid;
    private String userId;
    private String username;
    private String role;
}
