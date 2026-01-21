package com.spring.backend.Services.Booking;

import com.spring.backend.DTOs.Booking.BookingRequest;
import com.spring.backend.Exceptions.ActionNotAllowedException;
import com.spring.backend.Exceptions.ResourceNotFoundException;
import com.spring.backend.Models.Booking;
import com.spring.backend.Models.Showtime;
import com.spring.backend.Models.ShowtimeSeat;
import com.spring.backend.Models.User;
import com.spring.backend.Repositories.Booking.BookingRepository;
import com.spring.backend.Services.Showtime.ShowtimeService;
import com.spring.backend.Services.User.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {
    @Autowired
    private BookingRepository repository;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Autowired
    private ShowtimeService showtimeService;

    @Transactional
    public Booking createBooking(BookingRequest req) {
        User user = userService.getCurrentUser();
        Showtime showtime = showtimeService.getShowtimeById(req.getShowtimeId());

        List<ShowtimeSeat> availableSeats = showtime.getShowtimeSeats();
        List<ShowtimeSeat> selectedSeats = new ArrayList<>();
        int totalPrice = 0;
        for (String seatId : req.getSeatIds()) {
            ShowtimeSeat seat = availableSeats.stream()
                    .filter(s -> s.getSeat().getId().equals(seatId))
                    .findFirst()
                    .orElseThrow(() -> new ActionNotAllowedException("Seat " + seatId + " is not available."));
            totalPrice += seat.getPrice();
            selectedSeats.add(seat);
        }

        Booking booking = Booking.builder()
                .user(user)
                .showtime(showtime)
                .totalPrice(totalPrice)
                .status(0)
                .build();
        Booking newBooking = repository.save(booking);

        ticketService.createTickets(newBooking, selectedSeats);

        return newBooking;
    }

    @Transactional
    public boolean updateBookingAfterPayment(String id, boolean isSuccess) {

        Booking booking = repository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        if (isSuccess) {
            booking.setStatus(1);
            ticketService.updateTicketAfterPayment(booking);
        }

        repository.save(booking);
        return true;
    }
}
