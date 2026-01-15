package com.spring.backend.Models;

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
        private Long showtimeId;
        private Long seatId;
    }

    @EmbeddedId
    private ShowtimeSeatId id;

    private int status;
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;
}
