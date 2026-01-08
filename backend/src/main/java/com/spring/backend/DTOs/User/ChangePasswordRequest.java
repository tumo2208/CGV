package com.spring.backend.DTOs.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;
}
