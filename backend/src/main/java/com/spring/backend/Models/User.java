package com.spring.backend.Models;

import com.spring.backend.DTOs.User.UserDTO;
import com.spring.backend.DTOs.User.UserPartDTO;
import com.spring.backend.Enums.User.Gender;
import com.spring.backend.Enums.User.Provider;
import com.spring.backend.Enums.User.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true)
    private String phone;
    private String password;

    @Column(nullable = false)
    private String fullName;
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    private LocalDate dob;

    private String address;
    private String district;
    private String city;

    @Enumerated(value = EnumType.STRING)
    private Provider provider;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private String avatarUrl;
    private final LocalDateTime createdAt = LocalDateTime.now();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
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

    public UserDTO convertToDto() {
        return UserDTO.builder()
                .id(this.id)
                .email(this.email)
                .phone(this.phone)
                .fullName(this.fullName)
                .role(this.role)
                .dob(this.dob)
                .gender(this.gender)
                .address(this.address)
                .district(this.district)
                .city(this.city)
                .avatarUrl(this.avatarUrl)
                .createdAt(this.createdAt)
                .build();
    }

    public UserPartDTO convertToPartDTO() {
        return UserPartDTO.builder()
                .id(getId())
                .fullName(getFullName())
                .email(getEmail())
                .img(getAvatarUrl())
                .build();
    }
}
