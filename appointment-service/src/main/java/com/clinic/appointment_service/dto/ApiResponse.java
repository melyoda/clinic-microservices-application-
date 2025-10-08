package com.clinic.appointment_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    @JsonIgnore
    private HttpStatus httpStatus;

    private String message;
    private T data;

    // This getter will be called by Jackson to create the 'status' field in the JSON
    public String getStatus() {
        return httpStatus.toString();
    }

    // Optional: if you also want the numeric code exposed in the JSON
    public int getStatusCode() {
        return httpStatus.value();
    }

    // Helper methods for easy creation - UPDATED AS YOU SHOWED
    public static <T> ApiResponse<T> success(String message, T data, HttpStatus status) {
        return ApiResponse.<T>builder()
                .httpStatus(status) // Set the internal field
                .message(message)
                .data(data)
                .build();
    }

    public static ApiResponse<?> error(String message, HttpStatus status) {
        return ApiResponse.builder()
                .httpStatus(status) // Set the internal field
                .message(message)
                .data(null)
                .build();
    }
}