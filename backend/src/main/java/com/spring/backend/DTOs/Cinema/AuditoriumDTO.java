package com.spring.backend.DTOs.Cinema;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuditoriumDTO {
    private Long id;
    private String name;
    private String pattern;
}
