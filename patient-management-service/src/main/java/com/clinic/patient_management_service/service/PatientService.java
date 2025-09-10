package com.clinic.patient_management_service.service;

import com.clinic.patient_management_service.dto.PatientDTO;
import com.clinic.patient_management_service.dto.PatientResponseDTO;
import com.clinic.patient_management_service.exception.PatientAlreadyExistsException;
import com.clinic.patient_management_service.exception.PatientNotFoundException;
import com.clinic.patient_management_service.mapper.PatientMapper;
import com.clinic.patient_management_service.model.Patient;
import com.clinic.patient_management_service.repository.PatientRepository;
import com.clinic.patient_management_service.util.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {


    private final PatientRepository patientRepo;

    private final CurrentUser currentUser;

    public PatientResponseDTO addPatient(PatientDTO patientDTO) {
        // TODO: add user so we can also add the created by
        // TODO: add check if user national id is already in db

        if (patientRepo.existsByNationalId(patientDTO.getNationalId())) {
            throw new PatientAlreadyExistsException("A patient with national ID " + patientDTO.getNationalId() + " already exists.");
        }

        Patient patient = Patient.builder()
                .firstName(patientDTO.getFirstName())
                .lastName(patientDTO.getLastName())
                .patientGender(patientDTO.getGender())
                .nationalId(patientDTO.getNationalId())
                .dateOfBirth(patientDTO.getDateOfBirth())
                .phoneNumber(patientDTO.getPhoneNumber())
                .email(patientDTO.getEmail())
                .address(patientDTO.getAddress())
                .enteredBy(currentUser.getUsername())
                .enteredAt(LocalDateTime.now())
                .build();

        patientRepo.save(patient);
        return PatientMapper.toPatientResponseDTO(patient);

    }

    public void removePatient(Long patientId) {
        Patient patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + patientId));

        patientRepo.delete(patient);
    }

    // TODO: add a convenience update method using nationalId
    public PatientResponseDTO updatePatientById(Long patientId,PatientDTO patientDTO) {
        Patient patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + patientId));

        patient.setFirstName(patientDTO.getFirstName());
        patient.setLastName(patientDTO.getLastName());
        patient.setDateOfBirth(patientDTO.getDateOfBirth());
        patient.setPhoneNumber(patientDTO.getPhoneNumber());
        patient.setEmail(patientDTO.getEmail());
        patient.setAddress(patientDTO.getAddress());
        patient.setLastModifiedBy(currentUser.getUsername());
        patient.setLastModifiedAt(LocalDateTime.now());

        patientRepo.save(patient);
        return PatientMapper.toPatientResponseDTO(patient);

    }

//    public List<PatientResponseDTO> getAllPatients() {
//        // TODO: maybe make this return a page not a list
//        List<Patient> patients = patientRepo.findAll();
//        return PatientMapper.toPatientResponseDTOsList(patients);
//    }
    public Page<PatientResponseDTO> getAllPatients(Pageable pageable) {
        Page<Patient> patientsPage = patientRepo.findAll(pageable);
        return patientsPage.map(PatientMapper::toPatientResponseDTO);
    }

    public PatientResponseDTO getPatientByNationalID(String nationalId) {
        Patient patient =  patientRepo.findByNationalId(nationalId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with national ID: " + nationalId));

        return PatientMapper.toPatientResponseDTO(patient);
    }

    public List<PatientResponseDTO> getPatientByName(String firstname, String lastname) {
        List<Patient> patientList =  patientRepo
                .findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(firstname, lastname);

    if (patientList.isEmpty()) {
        throw new PatientNotFoundException("Patients not found with firstname " + firstname + " and lastname " + lastname);
    }

    return PatientMapper.toPatientResponseDTOsList(patientList);
    }



}
