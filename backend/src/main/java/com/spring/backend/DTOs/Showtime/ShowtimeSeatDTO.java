package com.spring.backend.DTOs.Showtime;

import com.spring.backend.Enums.Cinema.SeatType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShowtimeSeatDTO {
    private Long showtimeId;
    private Long seatId;
    private String seatNumber;
    private SeatType seatType;
    private int status;
    private int price;
}
