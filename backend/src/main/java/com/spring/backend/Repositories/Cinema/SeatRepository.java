package com.spring.backend.Repositories.Cinema;

import com.spring.backend.Models.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    void deleteAllByAuditorium_Id(Long auditoriumId);
}
