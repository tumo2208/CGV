package com.spring.backend.DTOs.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VerifyOtpRequest {
    private String email;
    private String otp;
}
