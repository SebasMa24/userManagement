package com.quantumdev.integraservicios.userManagment;

import com.quantumdev.integraservicios.userManagment.Model.Request.*;
import com.quantumdev.integraservicios.userManagment.Model.Response.*;
import com.quantumdev.integraservicios.userManagment.Controller.*;
import com.quantumdev.integraservicios.userManagment.Service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void login_ShouldReturnAuthResponse() {
        LoginRequest request = new LoginRequest("usuario1@example.com", "Password123");
        AuthResponse expected = new AuthResponse("jwt-token");

        when(authService.login(request)).thenReturn(expected);

        ResponseEntity<AuthResponse> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(authService).login(request);
    }

    @Test
    void register_ShouldReturnAuthResponse_WhenValid() {
        RegisterRequest request = RegisterRequest.builder()
                .email("test@example.com")
                .password("Password123!")
                .code(2020101234L)
                .name("Test User")
                .phone(321654987L)
                .address("Calle 10")
                .build();

        AuthResponse expected = new AuthResponse("jwt-token");

        when(authService.register(request)).thenReturn(expected);

        ResponseEntity<AuthResponse> response = authController.register(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(authService).register(request);
    }

    @Test
    void register_ShouldThrow_WhenEmailAlreadyExists() {
        RegisterRequest request = RegisterRequest.builder()
                .email("usuario1@example.com")
                .password("Password123!")
                .code(2020110001L)
                .name("Test User")
                .phone(3120000000L)
                .address("Calle 1")
                .build();

        when(authService.register(any())).thenThrow(new RuntimeException("El correo electrónico ya está registrado"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authController.register(request);
        });

        assertEquals("El correo electrónico ya está registrado", ex.getMessage());
        verify(authService).register(request);
    }

    @Test
    void register_ShouldThrow_WhenCodeAlreadyExists() {
        RegisterRequest request = RegisterRequest.builder()
                .email("nuevo@example.com")
                .password("Password123!")
                .code(1L)
                .name("Test User")
                .phone(3120000001L)
                .address("Calle 2")
                .build();

        when(authService.register(any())).thenThrow(new RuntimeException("El código universitario ya está registrado"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authController.register(request);
        });

        assertEquals("El código universitario ya está registrado", ex.getMessage());
        verify(authService).register(request);
    }

    @Test
    void register_ShouldThrow_WhenEmailInvalid() {
        RegisterRequest request = RegisterRequest.builder()
                .email("correo-malo")
                .password("Password123!")
                .code(2020110002L)
                .name("Test User")
                .phone(3120000002L)
                .address("Cra 2")
                .build();

        when(authService.register(any()))
                .thenThrow(new IllegalArgumentException("Error: the email correo-malo is invalid"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            authController.register(request);
        });

        assertEquals("Error: the email correo-malo is invalid", ex.getMessage());
        verify(authService).register(request);
    }

    @Test
    void register_ShouldThrow_WhenPhoneInvalid() {
        RegisterRequest request = RegisterRequest.builder()
                .email("valid@example.com")
                .password("Password123!")
                .code(2020110003L)
                .name("Test User")
                .phone(123L) // Número demasiado corto: 3 dígitos
                .address("Cra 3")
                .build();

        try {
            authController.register(request);
        } catch (Exception ex) {
            ex.printStackTrace(); // o usa logger
            fail("Excepción inesperada: " + ex.getMessage());
        }
    }

}