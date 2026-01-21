package com.spring.backend.Services.Showtime;

import com.spring.backend.DTOs.Showtime.ShowtimeSeatDTO;
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

    private int calculateSeatPrice(Showtime showtime, Seat seat) {
        int basePrice = showtime.getBasePrice();
        switch (seat.getSeatType()) {
            case VIP -> basePrice *= 1.5;
            case COUPLE -> basePrice *= 3.5;
        }
        return basePrice;
    }

    public void buildNewShowtimeSeats(Showtime showtime, List<Seat> seats) {
        List<ShowtimeSeat> showtimeSeats = seats.stream()
                .map(seat -> ShowtimeSeat.builder()
                        .id(new ShowtimeSeat.ShowtimeSeatId())
                        .showtime(showtime)
                        .seat(seat)
                        .status(0)
                        .price(calculateSeatPrice(showtime, seat))
                        .build())
                .toList();
        showtime.getShowtimeSeats().clear();
        showtime.getShowtimeSeats().addAll(reposistory.saveAll(showtimeSeats));
    }

    public void deleteShowtimeSeats(Showtime showtime) {
        reposistory.deleteAllById_ShowtimeId(showtime.getId());
    }

    public void rebuildShowtimeSeats(Showtime showtime, List<Seat> seats) {
        deleteShowtimeSeats(showtime);
        buildNewShowtimeSeats(showtime, seats);
    }

    public List<ShowtimeSeatDTO> getShowtimeSeatsByShowtimeId(Long showtimeId) {
        return reposistory.findById_ShowtimeId(showtimeId).stream().map(ShowtimeSeat::convertToDTO).toList();
    }

    public void updateShowtimeSeatsStatus(Long showtimeId, List<String> seatNumbers, int status) {
        List<ShowtimeSeat> showtimeSeats = seatNumbers.stream()
                .map(seat -> reposistory.findById_ShowtimeIdAndSeat_SeatNumber(showtimeId, seat))
                .toList();

        for (ShowtimeSeat showtimeSeat : showtimeSeats) {
            showtimeSeat.setStatus(status);
        }
    }
}