package com.spring.backend.Services.Cinema;

import com.spring.backend.Models.Auditorium;
import com.spring.backend.Models.Seat;
import com.spring.backend.Repositories.Cinema.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {
    @Autowired
    private SeatRepository reposistory;

    public void saveSeats(Auditorium auditorium) {
        List<Seat> seats = auditorium.getPattern().generateSeats();
        for (Seat seat : seats) {
            seat.setAuditorium(auditorium);
            auditorium.getSeats().add(seat);
        }
        reposistory.saveAll(seats);
    }

    public List<Seat> updateSeats(Auditorium auditorium) {
        // Remove existing seats
        reposistory.deleteAllByAuditorium_Id(auditorium.getId());

        // Generate new seating arrangement based on the updated pattern
        List<Seat> newSeats = auditorium.getPattern().generateSeats();
        for (Seat seat : newSeats) {
            seat.setAuditorium(auditorium);
            auditorium.getSeats().add(seat);
        }
        return reposistory.saveAll(newSeats);
    }
}