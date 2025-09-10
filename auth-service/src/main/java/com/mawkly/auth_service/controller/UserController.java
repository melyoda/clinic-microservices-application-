package com.mawkly.auth_service.controller;


import com.mawkly.auth_service.dto.*;
import com.mawkly.auth_service.model.User;
import com.mawkly.auth_service.model.UserPrincipal;
import com.mawkly.auth_service.repository.UserRepository;
import com.mawkly.auth_service.service.JwtService;
import com.mawkly.auth_service.service.MyUserDetailsService;
import com.mawkly.auth_service.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final JwtService jwtService;

    private final MyUserDetailsService userDetailsService; // The service that loads users
    private final UserRepository userRepository;           // The repository to fetch User entity

    @PostMapping("register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> createUser(@RequestBody RegisterRequestDTO registerRequest) {
            LoginResponseDTO loginResponse = service.createUser(registerRequest);

            ApiResponse<LoginResponseDTO> response = ApiResponse.<LoginResponseDTO>builder()
                    .httpStatus(HttpStatus.CREATED)
                    .message("User registration successful")
                    .data(loginResponse)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody LoginRequestDTO loginRequest) {
            LoginResponseDTO loginResponse = service.login(loginRequest);
            ApiResponse<LoginResponseDTO> response = ApiResponse.<LoginResponseDTO>builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Login successful")
                    .data(loginResponse)
                    .build();
            return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<AuthValidationResponse> validateToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthValidationResponse(false, null, null, null));
        }

        String token = authHeader.substring(7); // Extract the token

        try {
            // 1. Extract username and validate token
            String username = jwtService.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            boolean isTokenValid = jwtService.isTokenValid(token, userDetails);

            if (!isTokenValid) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthValidationResponse(false, null, null, null));
            }

            // 2. CAST to UserPrincipal to get the user ID and role
            UserPrincipal userPrincipal = (UserPrincipal) userDetails; // ✅ This is the key!
            User user = userPrincipal.getUser(); // ✅ Use the getter we just added

            // 3. Extract the information
            String userId = user.getId().toString();
            String usernameFromUser = user.getUsername();
            String role = user.getRole().name(); // e.g., "ADMIN"

            // 4. Return the successful response
            return ResponseEntity.ok(new AuthValidationResponse(true, userId, usernameFromUser, role));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthValidationResponse(false, null, null, null));
        }
    }
}
