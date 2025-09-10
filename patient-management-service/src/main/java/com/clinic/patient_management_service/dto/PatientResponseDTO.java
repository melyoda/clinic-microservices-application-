package com.clinic.patient_management_service.dto;

import com.clinic.patient_management_service.model.PatientGender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private PatientGender gender;
    private LocalDate dateOfBirth;
    private String nationalId;

    private String phoneNumber;
    private String email;
    private String address;

    // TODO: medical history
}
