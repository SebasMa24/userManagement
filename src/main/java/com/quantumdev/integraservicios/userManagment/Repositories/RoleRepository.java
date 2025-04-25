package com.quantumdev.integraservicios.userManagment.Repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.quantumdev.integraservicios.userManagment.Model.Entity.ERole;
import com.quantumdev.integraservicios.userManagment.Model.Entity.Role;

public interface RoleRepository extends JpaRepository<Role, ERole>{
    Optional<Role> findByName(ERole name);
}
