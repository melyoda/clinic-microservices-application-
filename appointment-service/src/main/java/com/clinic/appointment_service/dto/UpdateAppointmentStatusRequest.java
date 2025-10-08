package com.clinic.appointment_service.dto;

import com.clinic.appointment_service.model.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateAppointmentStatusRequest {
    @NotNull(message = "Status cannot be null")
    private AppointmentStatus status;
}
