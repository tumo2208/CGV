package com.spring.backend.DTOs.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangeForgotPasswordRequest {
    private String email;
    private String otp;
    private String newPassword;
}
