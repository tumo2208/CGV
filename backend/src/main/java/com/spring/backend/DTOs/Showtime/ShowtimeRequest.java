package com.spring.backend.DTOs.Showtime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowtimeRequest {
    private Long movieId;
    private Long auditoriumId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer basePrice;
}
