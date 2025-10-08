package com.clinic.appointment_service.mapper;

import com.clinic.appointment_service.dto.AppointmentResponse;
import com.clinic.appointment_service.dto.CreateAppointmentRequest;
import com.clinic.appointment_service.model.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public static AppointmentResponse toAppointmentResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatientId())
                .doctorId(appointment.getDoctorId())
                .createdBy(appointment.getCreatedBy())
                .dateTime(appointment.getDateTime())
                .type(appointment.getType())
                .status(appointment.getStatus())
                .createdAt(appointment.getCreatedAt())
                .build();
    }

}
