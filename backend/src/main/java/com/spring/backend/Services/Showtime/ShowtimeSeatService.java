package com.spring.backend.Services.Showtime;

import com.spring.backend.Models.Seat;
import com.spring.backend.Models.Showtime;
import com.spring.backend.Models.ShowtimeSeat;
import com.spring.backend.Repositories.Showtime.ShowtimeSeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowtimeSeatService {
    @Autowired
    private ShowtimeSeatRepository reposistory;

    public void rebuildShowtimeSeats(Showtime showtime, List<Seat> seats) {

        // Xóa toàn bộ showtime_seats cũ
        reposistory.deleteAllById_ShowtimeId(showtime.getId());

        // Tạo lại showtime_seats mới
        List<ShowtimeSeat> newShowtimeSeats = seats.stream()
                .map(seat -> ShowtimeSeat.builder()
                        .id(new ShowtimeSeat.ShowtimeSeatId(showtime.getId(), seat.getId()))
                        .status(0)
                        .price(showtime.getBasePrice())
                        .build())
                .toList();

        reposistory.saveAll(newShowtimeSeats);
    }
}