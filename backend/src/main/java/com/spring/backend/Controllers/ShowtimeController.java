package com.spring.backend.Controllers;

import com.spring.backend.DTOs.Showtime.ShowtimeDTO;
import com.spring.backend.Exceptions.ActionNotAllowedException;
import com.spring.backend.Exceptions.ResourceNotFoundException;
import com.spring.backend.Services.Showtime.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/showtimes")
public class ShowtimeController {
    @Autowired
    private ShowtimeService service;

    @PostMapping
    public ResponseEntity createShowtime(@RequestBody ShowtimeDTO req) {
        try {
            service.createShowtime(req);
            return ResponseEntity.ok().body("Showtime created successfully");
        } catch (ActionNotAllowedException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateShowtime(@RequestBody ShowtimeDTO req, @PathVariable Long id) {
        try {
            service.updateShowtime(id, req);
            return ResponseEntity.ok().body("Showtime updated successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteShowtime(@PathVariable Long id) {
        try {
            service.deleteShowtime(id);
            return ResponseEntity.ok().body("Showtime deleted successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getShowtimeById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(service.getShowtimeById(id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity getShowtimes(
            @RequestParam(required = false) Long movieId,
            @RequestParam(required = false) Long auditoriumId,
            @RequestParam(required = false) LocalDate date
            ) {
        try {
            return ResponseEntity.ok().body(service.findShowtimes(movieId, auditoriumId, date));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
