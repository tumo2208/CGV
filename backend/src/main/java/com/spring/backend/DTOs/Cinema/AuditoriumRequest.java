package com.spring.backend.DTOs.Cinema;


import com.spring.backend.Enums.Cinema.AuditoriumPattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriumRequest {
    private String name;
    private AuditoriumPattern pattern;
}
