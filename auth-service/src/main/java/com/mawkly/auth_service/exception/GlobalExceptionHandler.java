package com.mawkly.auth_service.exception;

import com.mawkly.auth_service.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j // Optional, for logging
public class GlobalExceptionHandler {

    // Handle bad requests (e.g., duplicate username, invalid argument)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("Bad request: {}", e.getMessage());
        ApiResponse<?> response = ApiResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle bad credentials specifically
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<?>> handleBadCredentials(BadCredentialsException e) {
        log.warn("Authentication failed: {}", e.getMessage());
        ApiResponse<?> response = ApiResponse.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .message("Invalid username or password")
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // Handle when a resource is not found (good for future endpoints like GET /users/{id})
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUsernameNotFound(UsernameNotFoundException e) {
        ApiResponse<?> response = ApiResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Handle any other unexpected exception (catch-all)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneralException(Exception e) {
        log.error("An unexpected error occurred: ", e); // Log the full stack trace for debugging
        ApiResponse<?> response = ApiResponse.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .message("An internal server error occurred. Please try again later.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}