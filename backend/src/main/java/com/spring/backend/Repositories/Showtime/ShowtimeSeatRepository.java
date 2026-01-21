package com.spring.backend.Repositories.Showtime;

import com.spring.backend.Models.ShowtimeSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShowtimeSeatRepository extends JpaRepository<ShowtimeSeat, ShowtimeSeat.ShowtimeSeatId> {
    void deleteAllById_ShowtimeId(Long showtimeId);
    List<ShowtimeSeat> findById_ShowtimeId(Long showtimeId);
    ShowtimeSeat findById_ShowtimeIdAndSeat_SeatNumber(Long showtimeId, String seatNumber);
}
