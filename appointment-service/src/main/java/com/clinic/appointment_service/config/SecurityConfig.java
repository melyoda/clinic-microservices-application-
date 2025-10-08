package com.clinic.appointment_service.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    public FilterRegistrationBean<TrustGatewayFilter> trustGatewayFilter() {
        FilterRegistrationBean<TrustGatewayFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TrustGatewayFilter());
        registrationBean.addUrlPatterns("/appointments/*");
        registrationBean.addUrlPatterns("/v3/api-docs/*", "/swagger-ui/*", "/swagger-ui.html");
        return registrationBean;
    }
}

