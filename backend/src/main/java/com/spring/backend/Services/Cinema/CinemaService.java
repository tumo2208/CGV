package com.spring.backend.Services.Cinema;

import com.spring.backend.DTOs.Cinema.AuditoriumDTO;
import com.spring.backend.DTOs.Cinema.CinemaDTO;
import com.spring.backend.DTOs.Cinema.CinemaRequest;
import com.spring.backend.Exceptions.ResourceNotFoundException;
import com.spring.backend.Models.Auditorium;
import com.spring.backend.Models.Cinema;
import com.spring.backend.Repositories.Cinema.CinemaRepository;
import com.spring.backend.Services.Common.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CinemaService {
    @Autowired
    private CinemaRepository reposistory;

//    @Autowired
//    private BookingReposistory bookingRepository;

    @Autowired
    private StorageService storageService;

    public void createCinema(CinemaRequest cinemaRequest) throws IOException {
        Cinema cinema = Cinema.builder()
                .name(cinemaRequest.getName())
                .address(cinemaRequest.getAddress())
                .district(cinemaRequest.getDistrict())
                .city(cinemaRequest.getCity())
                .phone(cinemaRequest.getPhone())
                .build();

        // Handle image uploads
        cinema.setImages(new ArrayList<>());
        if (cinemaRequest.getImages() != null) {
            for (MultipartFile image : cinemaRequest.getImages()) {
                String imageUrl = storageService.uploadImage(image);
                cinema.getImages().add(imageUrl);
            }
        }

        Cinema savedCinema = reposistory.save(cinema);
    }

    public CinemaDTO getCinema(long id) {
        Cinema cinema = reposistory.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Do not have cinema with id:" + id));
        return cinema.convertToDTO();
    }

    public List<CinemaDTO> getAllCinemas() {
        List<Cinema> cinemas = reposistory.findAll();
        return cinemas.stream()
                .map(Cinema::convertToDTO)
                .toList();
    }

    public List<CinemaDTO> getCinemasByCity(String city) {
        List<Cinema> cinemas = reposistory.findByCityIgnoreCase(city);
        return cinemas.stream()
                .map(Cinema::convertToDTO)
                .toList();
    }

    public void deleteCinema(long id) {
        Cinema cinema = reposistory.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Do not have cinema with id:" + id));
        reposistory.delete(cinema);
    }

    public void updateCinema(CinemaRequest cinemaRequest, long id) throws IOException{
        Cinema cinema = reposistory.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Do not have cinema with id:" + id));

        if (cinemaRequest.getName() != null) {
            cinema.setName(cinemaRequest.getName());
        }

        if (cinemaRequest.getAddress() != null) {
            cinema.setAddress(cinemaRequest.getAddress());
        }

        if (cinemaRequest.getDistrict() != null) {
            cinema.setDistrict(cinemaRequest.getDistrict());
        }

        if (cinemaRequest.getCity() != null) {
            cinema.setCity(cinemaRequest.getCity());
        }

        if (cinemaRequest.getPhone() != null) {
            cinema.setPhone(cinemaRequest.getPhone());
        }

        // Handle image uploads
        if (cinemaRequest.getImages() != null && !cinemaRequest.getImages().isEmpty()) {
            List<String> newImageUrls = new ArrayList<>();
            for (MultipartFile image : cinemaRequest.getImages()) {
                String imageUrl = storageService.uploadImage(image);
                newImageUrls.add(imageUrl);
            }
            cinema.setImages(newImageUrls);
        }

        reposistory.save(cinema);
    }

    public List<AuditoriumDTO> getAuditoriumsByCinemaId(Long cinemaId) {
        Cinema cinema = reposistory.findById(cinemaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cinema not found with id: " + cinemaId));
        return cinema.getAuditoriums().stream()
                .map(Auditorium::convertToDTO)
                .collect(Collectors.toList());
    }

//    public CinemaRevenueMonthlyDTO getRevenueByMonth(Long cinemaId, int year, int month) {
//
//        OffsetDateTime start = LocalDate.of(year, month, 1)
//                .atStartOfDay()
//                .atOffset(ZoneOffset.UTC);
//
//        OffsetDateTime end = start.plusMonths(1);
//
//        return bookingRepository.getCinemaRevenueByMonth(cinemaId, start, end);
//    }
}