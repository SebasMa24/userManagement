package com.quantumdev.integraservicios.userManagment.Service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.quantumdev.integraservicios.userManagment.Model.Entity.ERole;
import com.quantumdev.integraservicios.userManagment.Model.Entity.Role;
import com.quantumdev.integraservicios.userManagment.Model.Entity.User;
import com.quantumdev.integraservicios.userManagment.Model.Request.LoginRequest;
import com.quantumdev.integraservicios.userManagment.Model.Request.RegisterRequest;
import com.quantumdev.integraservicios.userManagment.Model.Response.AuthResponse;
import com.quantumdev.integraservicios.userManagment.Repositories.RoleRepository;
import com.quantumdev.integraservicios.userManagment.Repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        validateEmailFormat(request.getEmail());
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserDetails user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Error: User not found"));
        String token = jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse register(RegisterRequest request) {
        validateEmailFormat(request.getEmail());
        validatePhoneNumberFormat(request.getPhone());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }
        
        // Verificar si el código universitario ya está registrado
        if (request.getCode() != null && userRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("El código universitario ya está registrado");
        }

        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role not found"));

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .code(request.getCode())
                .name(request.getName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .role(userRole)
                .build();

        userRepository.save(user);

        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();
    }

    private void validateEmailFormat(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (email == null || !email.matches(emailRegex)) {
            throw new IllegalArgumentException("Error: the email " + email + " is invalid");
        }
    }

    private void validatePhoneNumberFormat(Long phone) {
        String phoneStr = String.valueOf(phone);
        if (phoneStr.length() < 7 || phoneStr.length() > 15) {
            throw new IllegalArgumentException("Error: the phone " + phone + " is invalid");
        }
    }
}
