package com.quantumdev.integraservicios.userManagment.Service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.quantumdev.integraservicios.userManagment.Model.Entity.Role;
import com.quantumdev.integraservicios.userManagment.Model.Entity.User;
import com.quantumdev.integraservicios.userManagment.Model.Request.LoginRequest;
import com.quantumdev.integraservicios.userManagment.Model.Request.RegisterRequest;
import com.quantumdev.integraservicios.userManagment.Model.Response.AuthResponse;
import com.quantumdev.integraservicios.userManagment.Repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserDetails user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token=jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .code(request.getCode())
            .name(request.getName())
            .address(request.getAddress())
            .phone(request.getPhone())
            .role(Role.USER)
            .build();

        userRepository.save(user);

        return AuthResponse.builder()
            .token(jwtService.getToken(user))
            .build();
    }

}
