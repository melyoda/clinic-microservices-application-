package com.clinic.patient_management_service.mapper;

import com.clinic.patient_management_service.dto.PatientResponseDTO;
import com.clinic.patient_management_service.model.Patient;

import java.util.List;
import java.util.stream.Collectors;

public class PatientMapper {

    public static PatientResponseDTO toPatientResponseDTO(Patient patient) {

        return PatientResponseDTO.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .gender(patient.getPatientGender())
                .dateOfBirth(patient.getDateOfBirth())
                .nationalId(patient.getNationalId())

                .phoneNumber(patient.getPhoneNumber())
                .email(patient.getEmail())
                .address(patient.getAddress())
                .build();

    }

    public static List<PatientResponseDTO> toPatientResponseDTOsList(List<Patient> patients) {
        return patients.stream()
                .map(PatientMapper::toPatientResponseDTO)
                .collect(Collectors.toList());
    }
}
