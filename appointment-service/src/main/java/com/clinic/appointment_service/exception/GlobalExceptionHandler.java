package com.clinic.appointment_service.exception;

import com.clinic.appointment_service.dto.ApiResponse;
import com.clinic.appointment_service.service.PatientServiceClient;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleAppointmentNotFound(AppointmentNotFoundException ex) {
        return new ResponseEntity<>(
                ApiResponse.builder().httpStatus(HttpStatus.NOT_FOUND).message(ex.getMessage()).build(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(PatientServiceClient.PatientNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handlePatientNotFoundFromClient(PatientServiceClient.PatientNotFoundException ex) {
        return new ResponseEntity<>(
                ApiResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message(ex.getMessage()).build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDenied(AccessDeniedException ex) {
        return new ResponseEntity<>(
                ApiResponse.builder().httpStatus(HttpStatus.FORBIDDEN).message(ex.getMessage()).build(),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception ex) {
//        return new ResponseEntity<>(
//                ApiResponse.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).message("An internal server error occurred.").build(),
//                HttpStatus.INTERNAL_SERVER_ERROR
//        );
        ex.printStackTrace(); // TEMP: log the real cause
        return new ResponseEntity<>(
                ApiResponse.error("An internal server error occurred.", HttpStatus.INTERNAL_SERVER_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }


}