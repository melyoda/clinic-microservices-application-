package com.clinic.appointment_service.service;

import com.clinic.appointment_service.dto.ApiResponse;
import com.clinic.appointment_service.dto.PatientResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PatientServiceClient {

    private final WebClient webClient;

    public PatientServiceClient(@Qualifier("patientServiceWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Boolean> existsByNationalId(String nationalId) {
        var attrs = RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = (attrs instanceof ServletRequestAttributes sra) ? sra.getRequest() : null;

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/patients/{nationalId}").build(nationalId))
                .headers(h -> {
                    if (req != null) {
                        var uid   = req.getHeader("X-User-Id");
                        var uname = req.getHeader("X-User-Name");
                        var roles = req.getHeader("X-User-Roles");
                        if (uid != null)   h.set("X-User-Id", uid);
                        if (uname != null) h.set("X-User-Name", uname);
                        if (roles != null) h.set("X-User-Roles", roles);
                    }
                })
                .exchangeToMono(resp -> {
                    if (resp.statusCode().is2xxSuccessful()) return Mono.just(true);
                    if (resp.statusCode().value() == 404)     return Mono.just(false);
                    return resp.createException().flatMap(Mono::error);
                });
    }
    /** Fetch full patient by nationalId and return DTO (includes id). */
    /** Fetch full patient (wrapped in ApiResponse) and return the DTO. */
    public Mono<PatientResponseDTO> getByNationalId(String nationalId) {
        // propagate gateway headers so patient-service authorizes us
        var attrs = RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = (attrs instanceof ServletRequestAttributes sra) ? sra.getRequest() : null;

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/patients/{nationalId}").build(nationalId))
                .headers(h -> {
                    if (req != null) {
                        String uid = req.getHeader("X-User-Id");
                        String uname = req.getHeader("X-User-Name");
                        String roles = req.getHeader("X-User-Roles");
                        if (uid != null)   h.set("X-User-Id", uid);
                        if (uname != null) h.set("X-User-Name", uname);
                        if (roles != null) h.set("X-User-Roles", roles);
                    }
                })
                .retrieve()
                .onStatus(s -> s.value() == 404,
                        r -> Mono.error(new PatientNotFoundException("Patient not found: " + nationalId)))
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<PatientResponseDTO>>() {})
                .map(ApiResponse::getData);   // <-- unwrap to DTO
    }

    public static class PatientNotFoundException extends RuntimeException {
        public PatientNotFoundException(String msg) { super(msg); }
    }
}
