package com.clinic.appointment_service.service;

import com.clinic.appointment_service.config.RabbitMQConfig;
import com.clinic.appointment_service.dto.AppointmentResponse;
import com.clinic.appointment_service.event.AppointmentEvent;
import com.clinic.appointment_service.mapper.AppointmentMapper;
import com.clinic.appointment_service.model.Appointment;
import com.clinic.appointment_service.model.AppointmentStatus;
import com.clinic.appointment_service.dto.CreateAppointmentRequest;
import com.clinic.appointment_service.repository.AppointmentRepository;
import com.clinic.appointment_service.util.CurrentUser;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientServiceClient patientServiceClient;
    private final RabbitTemplate rabbitTemplate;
    private final CurrentUser currentUser;

    @Transactional
    public AppointmentResponse createAppointment(CreateAppointmentRequest request) {

        if (request.getPatientNationalId() == null || request.getPatientNationalId().isBlank()) {
            throw new IllegalArgumentException("patientNationalId is required");
        }
        boolean exists = patientServiceClient.existsByNationalId(request.getPatientNationalId())
                .blockOptional().orElse(false);
        if (!exists) throw new PatientServiceClient.PatientNotFoundException(
                "Patient with nationalId " + request.getPatientNationalId() + " not found");


        // Create appointment with createdBy tracking
        Appointment appointment = Appointment.builder()
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .createdBy(currentUser.getUsername())
                .dateTime(request.getDateTime())
                .type(request.getType())
                .status(AppointmentStatus.SCHEDULED)
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);

        // Publish event
//        publishAppointmentEvent(savedAppointment, RabbitMQConfig.APPOINTMENT_SCHEDULED_ROUTING_KEY, "SCHEDULED");

        try {
            publishAppointmentEvent(savedAppointment,  RabbitMQConfig.APPOINTMENT_SCHEDULED_ROUTING_KEY, "SCHEDULED");
        } catch (Exception e) {
            System.err.println("Failed to publish event: " + e.getMessage());
        }

        return AppointmentMapper.toAppointmentResponse(savedAppointment);
    }

    /// change
    @Transactional
    public AppointmentResponse  updateAppointmentStatus(Long id, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with id: " + id));

        appointment.setStatus(status);
        Appointment updatedAppointment = appointmentRepository.save(appointment);

        // Publish event for cancellation or other status updates
        try {
            if (status == AppointmentStatus.CANCELLED) {
                publishAppointmentEvent(updatedAppointment, RabbitMQConfig.APPOINTMENT_CANCELLED_ROUTING_KEY, "CANCELLED");
            } else {
                publishAppointmentEvent(updatedAppointment, RabbitMQConfig.APPOINTMENT_UPDATED_ROUTING_KEY, "UPDATED");
            }
        } catch (Exception e) {
            System.err.println("Failed to publish event: " + e.getMessage());
        }

        return AppointmentMapper.toAppointmentResponse(updatedAppointment);
    }

    public List<AppointmentResponse> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId)
                .stream()
                .map(AppointmentMapper::toAppointmentResponse)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponse> getAppointmentsByDoctorAndDate(Long doctorId, LocalDate date) {
        return appointmentRepository.findByDoctorIdAndDateTimeBetween(
                doctorId,
                date.atStartOfDay(),
                date.atTime(LocalTime.MAX)
        )
                .stream()
                .map(AppointmentMapper::toAppointmentResponse)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponse> getAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId)
                .stream()
                .map(AppointmentMapper::toAppointmentResponse)
                .collect(Collectors.toList());
    }

    private void publishAppointmentEvent(Appointment appointment, String routingKey, String eventType) {
        AppointmentEvent event = new AppointmentEvent(
                eventType,
                appointment.getId(),
                appointment.getPatientId(),
                appointment.getDoctorId(),
                appointment.getDateTime(),
                appointment.getStatus()
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, routingKey, event);
    }
}
