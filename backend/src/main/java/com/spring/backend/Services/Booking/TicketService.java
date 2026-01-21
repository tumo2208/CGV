package com.spring.backend.Services.Booking;

import com.spring.backend.Models.Booking;
import com.spring.backend.Models.ShowtimeSeat;
import com.spring.backend.Models.Ticket;
import com.spring.backend.Repositories.Booking.TicketRepository;
import com.spring.backend.Services.Showtime.ShowtimeSeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {
    @Autowired
    private TicketRepository repository;

    @Autowired
    private ShowtimeSeatService showtimeSeatService;

    public void createTickets(Booking booking, List<ShowtimeSeat> selectedSeat) {
        List<Ticket> tickets = selectedSeat.stream().map(seat ->
            Ticket.builder()
                .booking(booking)
                .showtime(booking.getShowtime().getStartTime())
                .seatNumber(seat.getSeat().getSeatNumber())
                .price(seat.getPrice())
                .build()
        ).toList();
        booking.getTickets().addAll(repository.saveAll(tickets));

        showtimeSeatService.updateShowtimeSeatsStatus(booking.getShowtime().getId(),
                selectedSeat.stream().map(seat -> seat.getSeat().getSeatNumber()).toList(), 1);
    }

    public void updateTicketAfterPayment(Booking booking) {
        List<Ticket> tickets = repository.findByBookingId(booking.getId());
        for (Ticket ticket : tickets) {
            ticket.setStatus(1);
        }
        repository.saveAll(tickets);

        showtimeSeatService.updateShowtimeSeatsStatus(booking.getShowtime().getId(),
                tickets.stream().map(Ticket::getSeatNumber).toList(), 2);
    }
}
