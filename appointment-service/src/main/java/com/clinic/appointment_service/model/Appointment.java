package com.clinic.appointment_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Builder
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private Long doctorId;  // The doctor assigned to the appointment

    @Column(nullable = false)
    private String createdBy;  // The staff member who created the appointment (could be nurse, doctor, or admin)

    @Column(nullable = false)
    private LocalDateTime dateTime;  // Scheduled appointment time

    @Column(nullable = false)
    private String type; // e.g., "Check-up", "Consultation"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;  // When the appointment was created

    // Constructor - removed doctorId parameter, now using createdBy
//    public Appointment(Long patientId, Long createdBy, LocalDateTime dateTime, String type) {
//        this.patientId = patientId;
//        this.createdBy = createdBy;  // The user creating the appointment
//        this.dateTime = dateTime;
//        this.type = type;
//        this.status = AppointmentStatus.SCHEDULED; // Default status
//        this.createdAt = LocalDateTime.now();  // Auto-set creation time
//    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
