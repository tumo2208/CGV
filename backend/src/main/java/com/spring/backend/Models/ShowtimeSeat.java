package com.spring.backend.Models;

import com.spring.backend.DTOs.Showtime.ShowtimeSeatDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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
        @Column(name = "showtime_id")
        private Long showtimeId;

        @Column(name = "seat_id")
        private Long seatId;
    }

    @EmbeddedId
    private ShowtimeSeatId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("showtimeId")
    @JoinColumn(name = "showtime_id")
    private Showtime showtime;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("seatId")
    @JoinColumn(name = "seat_id")
    private Seat seat;

    private int status;
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    public ShowtimeSeatDTO convertToDTO() {
        return ShowtimeSeatDTO.builder()
                .showtimeId(this.id.getShowtimeId())
                .seatId(this.id.getSeatId())
                .seatNumber(this.seat.getSeatNumber())
                .seatType(this.seat.getSeatType())
                .status(this.status)
                .price(this.price)
                .build();
    }
}
