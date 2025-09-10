package com.clinic.patient_management_service.config;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    // Simple filter that trusts the gateway's headers
    @Bean
    public FilterRegistrationBean<TrustGatewayFilter> trustGatewayFilter() {
        FilterRegistrationBean<TrustGatewayFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TrustGatewayFilter());
        registrationBean.addUrlPatterns("/patients/*"); // Protect all patient endpoints
        registrationBean.addUrlPatterns("/v3/api-docs/*", "/swagger-ui/*", "/swagger-ui.html");
        return registrationBean;
    }
}

