package com.spring.backend.Controllers;

import com.spring.backend.DTOs.Movie.MovieDTO;
import com.spring.backend.Services.Movie.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/movies")
public class MovieController {
    @Autowired
    private MovieService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity createMovie(
            @RequestPart("movie") String movieJson,
            @RequestPart(value = "posterImage", required = false) MultipartFile posterImage
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            MovieDTO req = mapper.readValue(movieJson, MovieDTO.class);
            req.setPosterImage(posterImage);
            service.createMovie(req);
            return ResponseEntity.ok("Movie created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity changeMovieStatus(
            @PathVariable Long id,
            @RequestParam int newStatus) {
        try {
            service.changeMovieStatus(id, newStatus);
            return ResponseEntity.ok("Movie status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity updateMovie(
            @ModelAttribute MovieDTO req,
            @PathVariable Long id) {
        try {
            service.updateMovie(req, id);
            return ResponseEntity.ok("Movie updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity getMovies(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String releasedYear,
            @RequestParam(required = false) Integer status,

            @RequestParam(defaultValue = "releasedYear") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        try {
            return ResponseEntity.ok(
                    service.getMovies(genre, releasedYear, status, sortBy, sortDirection)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping(params = "keyword")
    public ResponseEntity searchMovies(@RequestParam String keyword) {
        try {
            return ResponseEntity.ok(service.searchMovies(keyword));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity deleteMovie(@PathVariable Long id) {
        try {
            service.deleteMovie(id);
            return ResponseEntity.ok("Movie deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }
}
