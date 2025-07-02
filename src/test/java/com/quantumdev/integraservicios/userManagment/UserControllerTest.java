package com.quantumdev.integraservicios.userManagment;

import com.quantumdev.integraservicios.userManagment.Controller.UserController;
import com.quantumdev.integraservicios.userManagment.Model.Request.SetAdminRequest;
import com.quantumdev.integraservicios.userManagment.Service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void setAdminRole_ShouldReturnOk_WhenMakeAdminTrue() {
        SetAdminRequest request = new SetAdminRequest("user@example.com", true);

        ResponseEntity<Void> response = userController.setAdminRole(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).setAdminRole("user@example.com", true);
    }

    @Test
    void setAdminRole_ShouldReturnOk_WhenMakeAdminFalse() {
        SetAdminRequest request = new SetAdminRequest("user@example.com", false);

        ResponseEntity<Void> response = userController.setAdminRole(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).setAdminRole("user@example.com", false);
    }

    @Test
    void setAdminRole_ShouldThrowException_WhenUserNotFound() {
        SetAdminRequest request = new SetAdminRequest("noexiste@example.com", true);

        doThrow(new RuntimeException("Error: User not found"))
                .when(userService).setAdminRole("noexiste@example.com", true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userController.setAdminRole(request);
        });

        assertEquals("Error: User not found", ex.getMessage());
        verify(userService).setAdminRole("noexiste@example.com", true);
    }

}
