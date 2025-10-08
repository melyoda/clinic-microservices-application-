package com.clinic.appointment_service.event;

import com.clinic.appointment_service.model.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentEvent implements Serializable {
    private String eventType; // e.g., "SCHEDULED", "CANCELLED"
    private Long appointmentId;
    private Long patientId;
    private Long doctorId;
    private LocalDateTime dateTime;
    private AppointmentStatus status;
}