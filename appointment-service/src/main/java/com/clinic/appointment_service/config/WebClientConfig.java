package com.clinic.appointment_service.config;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Value("${services.patient.url}")
    private String patientServiceUrl;

/*    @Bean
    public WebClient patientServiceWebClient() {
        return WebClient.builder()
                .baseUrl(patientServiceUrl)
                .build();
    }*/

    @Bean @Qualifier("patientServiceWebClient")
    public WebClient patientServiceWebClient() {
        return WebClient.builder()
                .baseUrl(patientServiceUrl)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .responseTimeout(Duration.ofSeconds(2))
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1500)))
                .build();
    }
}