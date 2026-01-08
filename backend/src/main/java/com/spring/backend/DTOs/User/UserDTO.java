package com.spring.backend.DTOs.User;

import com.spring.backend.Enums.User.Gender;
import com.spring.backend.Enums.User.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private Role role;
    private LocalDate dob;
    private Gender gender;
    private String address;
    private String district;
    private String city;

    private String avatarUrl;

    private LocalDateTime createdAt;
}