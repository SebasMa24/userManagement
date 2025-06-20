package com.quantumdev.integraservicios.userManagment.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quantumdev.integraservicios.userManagment.Model.Request.SetAdminRequest;
import com.quantumdev.integraservicios.userManagment.Service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    
    @PostMapping("/setAdminRole")
    public ResponseEntity<Void> setAdminRole(@RequestBody SetAdminRequest request) {
        userService.setAdminRole(request.getEmail(), request.isMakeAdmin());
        return ResponseEntity.ok().build();
    }
}
