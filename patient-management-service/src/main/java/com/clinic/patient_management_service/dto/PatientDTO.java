package com.clinic.patient_management_service.dto;

import com.clinic.patient_management_service.model.PatientGender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {

    @NotBlank(message = "First name cannot be empty.")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty.")
    private String lastName;

    @NotNull(message = "Gender must be specified.")
    private PatientGender gender;

    @NotBlank(message = "National ID is required.")
    @Pattern(regexp = "^[0-9]{8,15}$", message = "National ID must be between 8 and 15 digits.")
    private String nationalId;

    @NotNull(message = "Date of birth is required.")
    @Past(message = "Date of birth must be a date in the past.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "^[0-9\\-\\+]{9,15}$", message = "Phone number format is invalid.")
    private String phoneNumber;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email should be a valid email address.")
    private String email;

    @NotBlank(message = "Address cannot be empty.")
    private String address;
}

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class PatientDTO {
//    private String firstName;
//    private String lastName;
//    private PatientGender gender;
//    private String nationalId;
//    private LocalDate dateOfBirth;
//    private String phoneNumber;
//    private String email;
//    private String address;
//}


