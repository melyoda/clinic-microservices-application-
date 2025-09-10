package com.clinic.patient_management_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@Table(name = "patients")
@Data // Lombok for getters, setters, toString, etc.
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PatientGender patientGender;

    @Column(nullable = false, unique = true) // Real-world ID must be unique!
    private String nationalId; // or governmentId, idCardNumber

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    // Contact Info
    private String phoneNumber;
    private String email;
    private String address;

    private String enteredBy; // Username cuz the id is long and am too lazy to wrap it or turn the string to long
    private LocalDateTime enteredAt;

    private String lastModifiedBy;
    private LocalDateTime lastModifiedAt;

    // TODO: add medical history list
    // stats might be added in the treatment section of the application
    // Later: You can add lists for medical history, stats, etc.
    // @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<MedicalHistory> medicalHistory = new ArrayList<>();
}