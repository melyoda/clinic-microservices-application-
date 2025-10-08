package com.clinic.appointment_service.repository;


import com.clinic.appointment_service.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByDoctorIdAndDateTimeBetween(Long doctorId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<Appointment> findByDoctorId(Long doctorId);

}