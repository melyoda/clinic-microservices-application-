package com.clinic.appointment_service.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CreateAppointmentRequest {
    @NotNull(message = "Patient ID cannot be null")
    private Long patientId;

    @NotNull(message = "Doctor ID cannot be null")
    private Long doctorId;

    @NotNull(message = "Date and time cannot be null")
    @Future(message = "Appointment date must be in the future")
    private LocalDateTime dateTime;

    @NotNull(message = "Appointment type cannot be null")
    private String type;
}