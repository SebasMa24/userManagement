package com.quantumdev.integraservicios.userManagment.Service;

import org.springframework.stereotype.Service;

import com.quantumdev.integraservicios.userManagment.Model.Entity.ERole;
import com.quantumdev.integraservicios.userManagment.Model.Entity.Role;
import com.quantumdev.integraservicios.userManagment.Model.Entity.User;
import com.quantumdev.integraservicios.userManagment.Repositories.RoleRepository;
import com.quantumdev.integraservicios.userManagment.Repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public void setAdminRole(String email, boolean makeAdmin) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Error: User not found"));

        ERole targetRole = makeAdmin ? ERole.ROLE_ADMIN : ERole.ROLE_USER;

        Role adminRole = roleRepository.findByName(targetRole)
                .orElseThrow(() -> new RuntimeException("Error: role not found: " + targetRole));

        user.setRole(adminRole);
        userRepository.save(user);
    }
}
