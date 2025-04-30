package com.quantumdev.integraservicios.userManagment.Model.Entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "AppUser")
public class User implements UserDetails{

    @Id
    @Column(
        name = "email_user",
        length = 64,
        nullable = false
    )
    private String email;

    @ManyToOne(
        targetEntity = Role.class,
        optional = false
    )
    @JoinColumn(
        name = "role_user",
        nullable = false
    )
    private Role role;

    @Column(
        name = "pass_user",
        length = 32,
        nullable = false
    )
    private String password;

    @Column(name = "code_user")
    private Long code;

    @Column(
        name = "name_user",
        length = 32,
        nullable = false
    )
    private String name;

    @Column(
        name = "phone_user",
        nullable = false
    )
    private Long phone;

    @Column(
        name = "address_user",
        length = 64,
        nullable = false
    )
    private String address;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getName().name()));
    }
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
    
}
