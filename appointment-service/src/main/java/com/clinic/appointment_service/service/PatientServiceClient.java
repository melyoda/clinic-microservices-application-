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
                .uri("/patients/{id}/exists", patientId)
                .retrieve()
                .onStatus(s -> s.value()==404,
                        resp -> Mono.error(new PatientNotFoundException("Patient not found: " + patientId)))
                .toBodilessEntity()
                .map(e -> true)
                .onErrorResume(PatientNotFoundException.class, ex -> Mono.just(false));
    }

    /** Returns true if patient with given nationalId exists, false if 404, errors otherwise. */
    public Mono<Boolean> existsByNationalId(String nationalId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/patients/{nationalId}").build(nationalId))
                .exchangeToMono(resp -> {
                    if (resp.statusCode().is2xxSuccessful()) return Mono.just(true);
                    if (resp.statusCode().value() == 404)     return Mono.just(false);
                    return resp.createException().flatMap(Mono::error);
                });
    }

    // Custom exception for clarity
    public static class PatientNotFoundException extends RuntimeException {
        public PatientNotFoundException(String message) {
            super(message);
        }
    }
}
