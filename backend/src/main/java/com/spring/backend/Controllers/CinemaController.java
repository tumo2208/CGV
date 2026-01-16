package com.spring.backend.Controllers;

import com.spring.backend.DTOs.Cinema.AuditoriumRequest;
import com.spring.backend.DTOs.Cinema.CinemaDTO;
import com.spring.backend.DTOs.Cinema.CinemaRequest;
import com.spring.backend.Exceptions.ResourceNotFoundException;
import com.spring.backend.Services.Cinema.AuditoriumService;
import com.spring.backend.Services.Cinema.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cinemas")
public class CinemaController {
    @Autowired
    private CinemaService service;

    @Autowired
    private AuditoriumService auditoriumService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> createCinema(@ModelAttribute CinemaRequest req) {
        try {
            service.createCinema(req);
            return ResponseEntity.status(201).body("Create");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}/auditoriums")
    public ResponseEntity<?> createAuditorium(@PathVariable("id") Long CinemaId, @RequestBody AuditoriumRequest body) {
        try {
            auditoriumService.createAuditorium(body, CinemaId);
            return ResponseEntity.status(201).body("Create");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}/auditoriums")
    public ResponseEntity<?> getAuditoriumsByCinemaId(@PathVariable("id") Long CinemaId) {
        try {
            return ResponseEntity.status(200).body(service.getAuditoriumsByCinemaId(CinemaId));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCinema(@PathVariable Long id) {
        try {
            service.deleteCinema(id);
            return ResponseEntity.status(200).body("Deleted cinema with id: " + id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCinema(@PathVariable Long id, @ModelAttribute CinemaRequest req) {
        try {
            service.updateCinema(req, id);
            return ResponseEntity.status(200).body("Updated cinema with id: " + id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllCinemas() {
        try {
            return ResponseEntity.status(200).body(service.getAllCinemas());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCinemaById(@PathVariable Long id) {
        try {
            CinemaDTO response = service.getCinema(id);
            return ResponseEntity.status(200).body(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(params = "city")
    public ResponseEntity<?> getCinemasByCity(@RequestParam String city) {
        try {
            return ResponseEntity.status(200).body(service.getCinemasByCity(city));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @PreAuthorize("hasAuthority('LOCAL_ADMIN')")
//    @GetMapping("/{id}/revenue")
//    public ResponseEntity<?> getCinemaRevenueMonthly(@PathVariable Long id,
//                                                     @RequestParam int month,
//                                                     @RequestParam int year) {
//        try {
//            return ResponseEntity.status(200).body(service.getRevenueByMonth(id, year, month));
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(404).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
}