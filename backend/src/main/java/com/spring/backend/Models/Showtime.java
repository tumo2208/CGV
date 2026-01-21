package com.spring.backend.Models;

import com.spring.backend.DTOs.Showtime.ShowtimeDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "showtimes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Showtime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auditorium_id")
    private Auditorium auditorium;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private int basePrice;

    @OneToMany(mappedBy = "showtime", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ShowtimeSeat> showtimeSeats = new ArrayList<>();

    public ShowtimeDTO convertToDTO() {
        return ShowtimeDTO.builder()
                .id(this.id)
                .movieTitle(this.movie.getTitle())
                .auditoriumName(this.auditorium.getName())
                .cinemaName(this.auditorium.getCinema().getName())
                .startTime(this.startTime)
                .endTime(this.endTime)
                .basePrice(this.basePrice)
                .build();
    }
}
