package com.spring.backend.DTOs.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPartDTO {
    private Long id;
    private String fullName;
    private String email;
    private String img;
}