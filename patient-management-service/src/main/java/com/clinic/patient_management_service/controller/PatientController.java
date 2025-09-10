package com.clinic.patient_management_service.controller;

import com.clinic.patient_management_service.common.annotations.RequiredRole;
import com.clinic.patient_management_service.dto.ApiResponse;
import com.clinic.patient_management_service.dto.PatientDTO;
import com.clinic.patient_management_service.dto.PatientResponseDTO;
import com.clinic.patient_management_service.model.Patient;
import com.clinic.patient_management_service.service.PatientService;
import com.clinic.patient_management_service.util.GatewayAuthHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final GatewayAuthHelper authHelper;

    @PostMapping
    @RequiredRole({"ADMIN", "NURSE", "DOCTOR"})
    //remove admin later this is for testing for now
    public ResponseEntity<ApiResponse<PatientResponseDTO>> createPatient(
            @RequestBody PatientDTO patientDto
    ) {

        PatientResponseDTO patient = patientService.addPatient(patientDto);

        ApiResponse<PatientResponseDTO> apiResponse = ApiResponse.<PatientResponseDTO>builder()
                .httpStatus(HttpStatus.CREATED)
                .message("Patient created")
                .data(patient)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<String>> deletePatient(@PathVariable Long id) {
        patientService.removePatient(id);

            ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .httpStatus(HttpStatus.OK)
                .message("Patient deleted")
                .data(null)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<PatientResponseDTO>> updatePatient(
            @PathVariable Long id,
            @RequestBody PatientDTO patientDto
    ) {
        PatientResponseDTO patient = patientService.updatePatientById(id, patientDto);

        ApiResponse<PatientResponseDTO> apiResponse = ApiResponse.<PatientResponseDTO>builder()
                .httpStatus(HttpStatus.OK)
                .message("Patient updated")
                .data(patient)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("all-patients")
    public ResponseEntity<ApiResponse<Page<PatientResponseDTO>>> getAllPatients(Pageable pageable) {
        Page<PatientResponseDTO> patientsList = patientService.getAllPatients(pageable);

        ApiResponse<Page<PatientResponseDTO>> apiResponse = ApiResponse.<Page<PatientResponseDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .message("All patients")
                .data(patientsList)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("{nationalId}")
    // TODO: make this return a patientDTO
    public ResponseEntity<ApiResponse<PatientResponseDTO>> getPatient(@PathVariable String nationalId) {
        PatientResponseDTO patient = patientService.getPatientByNationalID(nationalId);

        ApiResponse<PatientResponseDTO> apiResponse = ApiResponse.<PatientResponseDTO>builder()
                .httpStatus(HttpStatus.OK)
                .message("Patient found")
                .data(patient)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<ApiResponse<List<PatientResponseDTO>>> findPatientFullName(
            @RequestParam String firstname,
            @RequestParam String lastname) {
        List<PatientResponseDTO> patientList = patientService.getPatientByName(firstname, lastname);

        ApiResponse<List<PatientResponseDTO>> apiResponse = ApiResponse.<List<PatientResponseDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .message("Patients found")
                .data(patientList)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}
