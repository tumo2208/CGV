package com.spring.backend.Repositories.Showtime;

import com.spring.backend.Models.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long>, JpaSpecificationExecutor<Showtime> {
    List<Showtime> findByAuditoriumIdAndStartTimeAfter(Long auditoriumId, LocalDateTime startTime);
}
