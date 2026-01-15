package com.spring.backend.Repositories.Showtime;

import com.spring.backend.Models.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    @Query("""
        SELECT s FROM Showtime s
        WHERE s.auditorium.id = :auditoriumId
        AND s.startTime > CURRENT_TIMESTAMP
    """)
    List<Showtime> findUpcomingShowtimesByAuditorium(@Param("auditoriumId") Long auditoriumId);
}
