package com.quantumdev.integraservicios.userManagment.Model.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="role")
public class Role {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name="name_role", nullable = false, unique = true)
    ERole name;

    @Column(name="desc_role", nullable = false)
    String description;
}
