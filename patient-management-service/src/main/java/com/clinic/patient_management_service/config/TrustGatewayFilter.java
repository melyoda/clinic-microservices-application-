package com.clinic.patient_management_service.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;

public class TrustGatewayFilter implements Filter {


    private static final List<String> PUBLIC_PATHS = List.of(
            "/v3/api-docs",
            "/swagger-ui"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String path = httpRequest.getRequestURI();
        if (PUBLIC_PATHS.stream().anyMatch(path::contains)) {
            chain.doFilter(request, response); // Let the request pass
            return; // Stop further processing in this filter
        }

        // Check if the gateway added the authentication headers
        if (httpRequest.getHeader("X-User-Id") == null) {
            ((HttpServletResponse) response).setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Missing gateway authentication headers\"}");
            return;
        }

        // If headers are present, the gateway has already authenticated this request
        chain.doFilter(request, response);
    }
}
