package com.clinic.patient_management_service.exception;

import com.clinic.patient_management_service.dto.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handlePatientNotFound(PatientNotFoundException ex) {
        ApiResponse<?> response = ApiResponse.error(
                ex.getMessage(),
                HttpStatus.NOT_FOUND // Pass the status here
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PatientAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<?>> handlePatientAlreadyExistsException(PatientAlreadyExistsException ex) {
        ApiResponse<?> response = ApiResponse.error(
//                ex.getMessage(),
                "A patient with this National ID already exists.", // User-friendly message
                HttpStatus.CONFLICT // Pass the status here
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // Handle other specific exceptions...
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        // Log the full exception for debugging: log.error("Data integrity violation", ex);
        ApiResponse<?> response = ApiResponse.error(
                "A patient with this National ID already exists.", // User-friendly message
                HttpStatus.CONFLICT
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // A catch-all for any other exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception ex) {
        ApiResponse<?> response = ApiResponse.error(
                "An internal server error occurred.",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); // 500
    }
}