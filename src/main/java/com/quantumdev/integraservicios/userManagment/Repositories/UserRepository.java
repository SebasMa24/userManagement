package com.quantumdev.integraservicios.userManagment.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quantumdev.integraservicios.userManagment.Model.Entity.User;

public interface UserRepository extends JpaRepository<User,String>{
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByCode(Long code);
}
