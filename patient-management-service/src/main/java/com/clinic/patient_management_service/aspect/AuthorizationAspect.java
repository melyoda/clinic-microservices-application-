package com.clinic.patient_management_service.aspect;

import com.clinic.patient_management_service.common.annotations.RequiredRole;
import com.clinic.patient_management_service.util.GatewayAuthHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizationAspect {

    private final GatewayAuthHelper authHelper;

    @Around("@annotation(requiredRole)")
    public Object checkRoles(ProceedingJoinPoint joinPoint, RequiredRole requiredRole) throws Throwable {
        HttpServletRequest request = getCurrentRequest();
        String userRoles = request.getHeader("X-User-Roles");

        if (userRoles == null) {
            throw new AccessDeniedException("No roles found in request");
        }

        // Check if user has ANY of the required roles
        boolean hasRequiredRole = Arrays.stream(requiredRole.value())
                .anyMatch(required -> userRoles.contains(required));

        if (!hasRequiredRole) {
            throw new AccessDeniedException("Insufficient permissions. Required one of: " +
                    Arrays.toString(requiredRole.value()));
        }

        return joinPoint.proceed();
    }

    private HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }
}