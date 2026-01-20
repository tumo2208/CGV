package com.spring.backend.Models;

import com.spring.backend.DTOs.Showtime.ShowtimeSeatDTO;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "showtime_seats")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowtimeSeat {
    @Embeddable
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShowtimeSeatId implements Serializable {
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "showtime_id")
        private Showtime showtime;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "seat_id")
        private Seat seatId;
    }

    @EmbeddedId
    private ShowtimeSeatId id;

    private int status;
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    public ShowtimeSeatDTO convertToDTO() {
        return ShowtimeSeatDTO.builder()
                .showtimeId(this.id.getShowtime().getId())
                .seatId(this.id.getSeatId().getId())
                .seatNumber(this.id.getSeatId().getSeatNumber())
                .seatType(this.id.getSeatId().getSeatType())
                .status(this.status)
                .price(this.price)
                .build();
    }
}
