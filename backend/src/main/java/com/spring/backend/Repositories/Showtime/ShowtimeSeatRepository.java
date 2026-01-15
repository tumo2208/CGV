package com.spring.backend.Repositories.Showtime;

import com.spring.backend.Models.ShowtimeSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowtimeSeatRepository extends JpaRepository<ShowtimeSeat, ShowtimeSeat.ShowtimeSeatId> {
    void deleteAllById_ShowtimeId(Long showtimeId);
}
