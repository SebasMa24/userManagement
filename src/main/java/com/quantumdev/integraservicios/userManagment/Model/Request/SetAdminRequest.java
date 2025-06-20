package com.quantumdev.integraservicios.userManagment.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetAdminRequest {
    private String email;
    private boolean makeAdmin;
}
