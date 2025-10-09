package com.clinic.appointment_service.config;

import com.clinic.appointment_service.util.CurrentUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CurrentUserFilter extends OncePerRequestFilter {

    private final CurrentUser currentUser;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws IOException, ServletException {

        String username = request.getHeader("X-User-Name");
        if (username != null && currentUser != null) {
            currentUser.setUsername(username.trim());
        }
        chain.doFilter(request, response);
    }
}