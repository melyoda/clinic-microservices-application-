package com.clinic.appointment_service.controller;

import com.clinic.appointment_service.common.annotations.RequiredRole;
import com.clinic.appointment_service.dto.ApiResponse;
import com.clinic.appointment_service.dto.AppointmentResponse;
import com.clinic.appointment_service.model.Appointment;
import com.clinic.appointment_service.dto.CreateAppointmentRequest;
import com.clinic.appointment_service.dto.UpdateAppointmentStatusRequest;
import com.clinic.appointment_service.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;


    @PostMapping
    @RequiredRole({"NURSE", "DOCTOR", "ADMIN"})  // All staff roles can create appointments
    public ResponseEntity<ApiResponse<AppointmentResponse>> createAppointment(
            @Valid @RequestBody CreateAppointmentRequest request,
            @RequestHeader("X-User-Id") Long createdByUserId,
            @RequestHeader("X-User-Roles") String userRoles) {

        // Additional business logic based on roles
        if (userRoles.contains("NURSE") && request.getDoctorId() == null) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Nurses must specify a doctor for the appointment", HttpStatus.BAD_REQUEST)
            );
        }

        Appointment appointment = appointmentService.createAppointment(request, createdByUserId);
        AppointmentResponse response = mapToResponse(appointment);

        return ResponseEntity.ok(ApiResponse.success("Appointment created", response, HttpStatus.CREATED));
    }
}
