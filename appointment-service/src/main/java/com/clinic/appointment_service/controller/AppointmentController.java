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
    @RequiredRole({"NURSE", "DOCTOR"})
    public ResponseEntity<ApiResponse<AppointmentResponse>> createAppointment(
            @Valid @RequestBody CreateAppointmentRequest request) {

        AppointmentResponse response = appointmentService.createAppointment(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Appointment created", response, HttpStatus.CREATED));
    }

    @PatchMapping("/{id}/status")
    @RequiredRole({"NURSE", "DOCTOR"})
    public ResponseEntity<ApiResponse<AppointmentResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAppointmentStatusRequest request) {

        AppointmentResponse response = appointmentService.updateAppointmentStatus(id, request.getStatus());
        return ResponseEntity.ok(ApiResponse.success("Appointment status updated", response, HttpStatus.OK));
    }

    @GetMapping("/patient/{patientId}")
    @RequiredRole({"NURSE", "DOCTOR", "ADMIN"})
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getPatientAppointments(
            @PathVariable Long patientId) {

        List<AppointmentResponse> appointments = appointmentService.getAppointmentsByPatient(patientId);
        return ResponseEntity.ok(ApiResponse.success("Patient appointments retrieved", appointments, HttpStatus.OK));
    }

    @GetMapping("/doctor/{doctorId}/date/{date}")
    @RequiredRole({"NURSE", "DOCTOR", "ADMIN"})
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getDoctorAppointmentsByDate(
            @PathVariable Long doctorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<AppointmentResponse> appointments = appointmentService.getAppointmentsByDoctorAndDate(doctorId, date);
        return ResponseEntity.ok(ApiResponse.success("Doctor appointments for date retrieved", appointments, HttpStatus.OK));
    }

    @GetMapping("/doctor/{doctorId}")
    @RequiredRole({"NURSE", "DOCTOR", "ADMIN"})
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getDoctorAppointments(
            @PathVariable Long doctorId) {

        List<AppointmentResponse> appointments = appointmentService.getAppointmentsByDoctor(doctorId);
        return ResponseEntity.ok(ApiResponse.success("Doctor appointments retrieved", appointments, HttpStatus.OK));
    }
}
