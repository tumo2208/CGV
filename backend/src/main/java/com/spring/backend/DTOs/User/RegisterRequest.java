package com.spring.backend.DTOs.User;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@Builder
public class RegisterRequest {
    private String email;
    private String phone;
    private String password;
    private String fullName;
    private String gender;
    private LocalDate dob;
    private String address;
    private String district;
    private String city;
    private MultipartFile avatar;
}
