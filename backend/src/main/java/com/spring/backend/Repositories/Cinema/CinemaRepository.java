package com.spring.backend.Repositories.Cinema;

import com.spring.backend.Models.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CinemaRepository extends JpaRepository<Cinema, Long> {
    List<Cinema> findByCityIgnoreCase(String city);
}
