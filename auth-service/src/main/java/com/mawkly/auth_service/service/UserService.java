package com.mawkly.auth_service.service;

import com.mawkly.auth_service.dto.LoginRequestDTO;
import com.mawkly.auth_service.dto.LoginResponseDTO;
import com.mawkly.auth_service.dto.RegisterRequestDTO;
import com.mawkly.auth_service.dto.UserAccountDTO;
import com.mawkly.auth_service.model.User;
import com.mawkly.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private UserRepository userRepo;

    private final PasswordEncoder passwordEncoder;
//    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public LoginResponseDTO createUser(RegisterRequestDTO registerRequest) {

        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword())) // Hash the password
                .role(registerRequest.getRole())
                .build();

        // Check for duplicate first, or let the DataIntegrityViolationException be thrown
        if (userRepo.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        userRepo.save(user);
//        try {
//            userRepo.save(user);
//        } catch (Exception e) {
//            throw new RuntimeException("Internal server error: "+ e.getMessage());
//        }
//        return jwtService.generateToken(user.getEmail());
        String token = jwtService.generateToken(user.getUsername());
        UserAccountDTO userAccountDTO = convertToUserAccountDTO(user);

        return new LoginResponseDTO(token, userAccountDTO);

    }


    // This method returns the token or throws an exception if authentication fails
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        // The authenticate method will throw an exception if credentials are bad
        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        if (authentication.isAuthenticated()) {
            // Get user from database
            User user = userRepo.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new BadCredentialsException("User not found"));

            // Generate token and convert user to DTO
            String token = jwtService.generateToken(loginRequest.getUsername());
            UserAccountDTO userAccountDTO = convertToUserAccountDTO(user);

            return new LoginResponseDTO(token, userAccountDTO);
        }
        // This part is technically unreachable if auth fails, as it throws an exception first
        throw new BadCredentialsException("Invalid username or password");
    }

    private UserAccountDTO convertToUserAccountDTO(User user) {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setId(user.getId());
        userAccountDTO.setUsername(user.getUsername());
        userAccountDTO.setRole(user.getRole());

        return userAccountDTO;
    }

}
