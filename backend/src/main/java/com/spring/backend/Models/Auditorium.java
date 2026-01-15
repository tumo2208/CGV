package com.spring.backend.Models;

import com.spring.backend.DTOs.Cinema.AuditoriumDTO;
import com.spring.backend.Enums.Cinema.AuditoriumPattern;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "auditoriums")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Auditorium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private AuditoriumPattern pattern;

    @OneToMany(mappedBy = "auditorium", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Seat> seats = new ArrayList<>();

    @OneToMany(mappedBy = "auditorium", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Showtime> showtimes = new ArrayList<>();

    public AuditoriumDTO convertToDTO() {
        return AuditoriumDTO.builder()
                .id(this.id)
                .name(this.name)
                .pattern(this.pattern.name())
                .build();
    }
}
