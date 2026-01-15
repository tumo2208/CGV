package com.spring.backend.Services.Cinema;

import com.spring.backend.DTOs.Cinema.AuditoriumDTO;
import com.spring.backend.DTOs.Cinema.AuditoriumRequest;
import com.spring.backend.Enums.Cinema.AuditoriumPattern;
import com.spring.backend.Exceptions.ResourceNotFoundException;
import com.spring.backend.Models.Auditorium;
import com.spring.backend.Models.Cinema;
import com.spring.backend.Models.Seat;
import com.spring.backend.Models.Showtime;
import com.spring.backend.Repositories.Cinema.AuditoriumRepository;
import com.spring.backend.Repositories.Cinema.CinemaRepository;
import com.spring.backend.Repositories.Showtime.ShowtimeRepository;
import com.spring.backend.Services.Showtime.ShowtimeSeatService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuditoriumService {
    @Autowired
    private AuditoriumRepository reposistory;

    @Autowired
    private SeatService seatService;

    @Autowired
    private ShowtimeSeatService showtimeSeatService;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private CinemaRepository cinemaRepository;

    @Transactional
    public void createAuditorium(AuditoriumRequest auditoriumRequest, Long cinemaId) {
        Auditorium auditorium = Auditorium.builder()
                .name(auditoriumRequest.getName())
                .pattern(auditoriumRequest.getPattern())
                .seats(new ArrayList<>())
                .build();
        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cinema not found with id: " + cinemaId));
        auditorium.setCinema(cinema);
        cinema.getAuditoriums().add(auditorium);
        Auditorium savedAuditorium = reposistory.save(auditorium);

        // Generate seating arrangement based on the auditorium pattern
        seatService.saveSeats(savedAuditorium);
    }

    public AuditoriumDTO getAuditoriumById(Long auditoriumId) {
        Auditorium auditorium = reposistory.findById(auditoriumId)
                .orElseThrow(() -> new ResourceNotFoundException("Auditorium not found with id: " + auditoriumId));
        return auditorium.convertToDTO();
    }

    public void deleteAuditorium(Long auditoriumId) {
        Auditorium auditorium = reposistory.findById(auditoriumId)
                .orElseThrow(() -> new ResourceNotFoundException("Auditorium not found with id: " + auditoriumId));
        reposistory.delete(auditorium);
    }

    @Transactional
    public void updateAuditoriumPattern(Long auditoriumId, String newPattern) {
        Auditorium auditorium = reposistory.findById(auditoriumId)
                .orElseThrow(() -> new ResourceNotFoundException("Auditorium not found with id: " + auditoriumId));
        auditorium.setPattern(AuditoriumPattern.valueOf(newPattern));
        Auditorium updatedAuditorium = reposistory.save(auditorium);

        // Update seating arrangement based on the new pattern
        List<Seat> updatedSeats = seatService.updateSeats(auditorium);

        List<Showtime> showtimes = showtimeRepository.findUpcomingShowtimesByAuditorium(auditoriumId);

        if (showtimes == null || showtimes.isEmpty()) {
            return;
        }

        for (Showtime showtime : showtimes) {
            showtimeSeatService.rebuildShowtimeSeats(showtime, updatedSeats);
        }
    }
}
