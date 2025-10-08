package com.clinic.appointment_service.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PatientServiceClient {

    private final WebClient webClient;

    public PatientServiceClient(@Qualifier("patientServiceWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Boolean> patientExists(Long patientId) {
        return webClient.get()
                .uri("/patients/{nationalId}", patientId) // Assuming patient service has this endpoint
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.just(new PatientNotFoundException("Patient not found: " + patientId)))
                .toBodilessEntity()
                .flatMap(response -> Mono.just(response.getStatusCode().is2xxSuccessful()));
    }

    // Custom exception for clarity
    public static class PatientNotFoundException extends RuntimeException {
        public PatientNotFoundException(String message) {
            super(message);
        }
    }
}
