package com.clinic.appointment_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${services.patient.url}")
    private String patientServiceUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    @Bean
    public WebClient patientServiceWebClient() {
        return WebClient.builder()
                .baseUrl(patientServiceUrl)
                .build();
    }
}