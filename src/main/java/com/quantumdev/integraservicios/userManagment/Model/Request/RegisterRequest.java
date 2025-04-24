package com.quantumdev.integraservicios.userManagment.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    String email;
    String password;
    String code;
    String name;
    String phone;
    String address;
}
