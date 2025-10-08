package com.clinic.appointment_service.dto;

import com.clinic.appointment_service.model.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private Long id;
    private Long patientId;
    private Long doctorId;        // Assigned doctor for the appointment
    private Long createdBy;       // Who created this appointment
    private LocalDateTime dateTime;
    private String type;
    private AppointmentStatus status;
    private LocalDateTime createdAt;  // When it was created in the system
}