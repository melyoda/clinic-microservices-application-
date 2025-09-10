package com.clinic.patient_management_service.repository;

import com.clinic.patient_management_service.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository  extends JpaRepository<Patient, Long> {

    // Perfect for finding by national ID card number
    Optional<Patient> findByNationalId(String nationalId);

    boolean existsByNationalId(String nationalId);

    // Find by name - returns a list because many patients can have the same name
    List<Patient> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);
}