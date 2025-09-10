package com.clinic.patient_management_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

    // Needed if you have other services calling you (optional, but good practice)
    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
//    @Bean
//    public WebClient webClient() {
//        return WebClient.builder()
//                .defaultStatusHandler(HttpStatusCode::isError, resp -> {
//                    // This helps handle 4xx/5xx errors from the Auth service gracefully
//                    return Mono.error(new RuntimeException("Auth service call failed"));
//                })
//                .build();
//    }
