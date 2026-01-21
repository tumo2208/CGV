package com.spring.backend.DTOs.Booking;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketDTO {
    private String id;
    private String bookingId;
    private String seatNumber;
    private Integer price;
}
