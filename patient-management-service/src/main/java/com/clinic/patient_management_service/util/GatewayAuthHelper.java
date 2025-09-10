package com.clinic.patient_management_service.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class GatewayAuthHelper {

    public boolean hasRole(HttpServletRequest request, String requiredRole) {
        String userRoles = request.getHeader("X-User-Roles");
        return userRoles != null && userRoles.contains(requiredRole);
    }

    public String getUserId(HttpServletRequest request) {
        return request.getHeader("X-User-Id");
    }

    public String getUsername(HttpServletRequest request) {
        return request.getHeader("X-User-Name"); // This comes from the gateway
    }
}
