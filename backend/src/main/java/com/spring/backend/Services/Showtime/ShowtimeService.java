package com.spring.backend.Services.Showtime;

import com.spring.backend.DTOs.Showtime.ShowtimeDTO;
import com.spring.backend.DTOs.Showtime.ShowtimeRequest;
import com.spring.backend.DTOs.Showtime.ShowtimeSeatDTO;
import com.spring.backend.Exceptions.ActionNotAllowedException;
import com.spring.backend.Exceptions.ResourceNotFoundException;
import com.spring.backend.Models.Auditorium;
import com.spring.backend.Models.Movie;
import com.spring.backend.Models.Showtime;
import com.spring.backend.Repositories.Cinema.AuditoriumRepository;
import com.spring.backend.Repositories.Movie.MovieRepository;
import com.spring.backend.Repositories.Showtime.ShowtimeRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShowtimeService {
    @Autowired
    private ShowtimeRepository repository;

    @Autowired
    private AuditoriumRepository auditoriumRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowtimeSeatService showtimeSeatService;

    @Transactional
    public void createShowtime(ShowtimeRequest req) {
        if (req.getAuditoriumId() == null || req.getMovieId() == null) {
            throw new ActionNotAllowedException("Auditorium ID and Movie ID must not be null");
        }

        Auditorium auditorium = auditoriumRepository.findById(req.getAuditoriumId())
                .orElseThrow(() -> new ResourceNotFoundException("Auditorium not found with id: " + req.getAuditoriumId()));

        Movie movie = movieRepository.findById(req.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + req.getMovieId()));

        Showtime showtime = Showtime.builder()
                .auditorium(auditorium)
                .movie(movie)
                .startTime(req.getStartTime())
                .endTime(req.getEndTime())
                .basePrice(req.getBasePrice())
                .build();
        Showtime newShowtime = repository.save(showtime);

        showtimeSeatService.buildNewShowtimeSeats(newShowtime, auditorium.getSeats());
    }

    @Transactional
    public void updateShowtime(Long showtimeId, ShowtimeRequest req) {
        Showtime showtime = repository.findById(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + showtimeId));

        if (req.getStartTime() != null) {
            showtime.setStartTime(req.getStartTime());
        }
        if (req.getEndTime() != null) {
            showtime.setEndTime(req.getEndTime());
        }
        if (req.getBasePrice() != 0) {
            showtime.setBasePrice(req.getBasePrice());
        }

        if (req.getAuditoriumId() != null && auditoriumRepository.existsById(req.getAuditoriumId())) {
            showtime.setAuditorium(auditoriumRepository.findById(req.getAuditoriumId()).get());

            if (req.getAuditoriumId() != showtime.getAuditorium().getId()) {
                showtimeSeatService.rebuildShowtimeSeats(showtime, showtime.getAuditorium().getSeats());
            }
        }

        if (req.getMovieId() != null && movieRepository.existsById(req.getMovieId())) {
            showtime.setMovie(movieRepository.findById(req.getMovieId()).get());
        }

        repository.save(showtime);
    }

    @Transactional
    public void deleteShowtime(Long showtimeId) {
        Showtime showtime = repository.findById(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + showtimeId));

        showtimeSeatService.deleteShowtimeSeats(showtime);

        repository.delete(showtime);


    }

    public List<ShowtimeDTO> findShowtimes(Long movieId, Long cinemaId, LocalDate date) {

        Specification<Showtime> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Lọc theo Movie ID
            if (movieId != null) {
                predicates.add(cb.equal(root.get("movie").get("id"), movieId));
            }

            // 2. Lọc theo Cinema ID
            if (cinemaId != null) {
                predicates.add(cb.equal(root.get("auditorium").get("cinema").get("id"), cinemaId));
            }

            // 3. Lọc theo Date
            if (date != null) {
                LocalDateTime startOfDay = date.atStartOfDay();
                LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

                predicates.add(cb.between(root.get("startTime"), startOfDay, endOfDay));
            }

            // 4. Sắp xếp theo Start Time (Tăng dần)
            query.orderBy(cb.asc(root.get("startTime")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return repository.findAll(spec).stream().map(Showtime::convertToDTO).toList();
    }

    public ShowtimeDTO getShowtimeDTOById(Long showtimeId) {
        Showtime showtime = repository.findById(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + showtimeId));
        return showtime.convertToDTO();
    }

    public Showtime getShowtimeById(Long showtimeId) {
        return repository.findById(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + showtimeId));
    }

    public List<ShowtimeSeatDTO> getShowtimeSeats(Long showtimeId) {
        if (!repository.existsById(showtimeId)) {
            throw new ResourceNotFoundException("Showtime not found with id: " + showtimeId);
        }
        return showtimeSeatService.getShowtimeSeatsByShowtimeId(showtimeId);
    }
}
