package com.spring.backend.Services.Movie;

import com.spring.backend.DTOs.Movie.MovieDTO;
import com.spring.backend.Exceptions.ResourceNotFoundException;
import com.spring.backend.Models.Movie;
import com.spring.backend.Repositories.Movie.MovieRepository;
import com.spring.backend.Services.Common.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

@Service
public class MovieService {
    @Autowired
    private MovieRepository repository;

    @Autowired
    private StorageService storageService;

    public void createMovie(MovieDTO request) throws IOException {
        String posterUrl = request.getPosterLink();
        if ((posterUrl == null || posterUrl.isEmpty()) && request.getPosterImage() != null) {
            posterUrl = storageService.uploadImage(request.getPosterImage());
        }

        Movie movie = Movie.builder()
                .title(request.getTitle())
                .releasedYear(request.getReleasedYear())
                .runtime(request.getRuntime())
                .genre(request.getGenre())
                .imdbRating(request.getImdbRating())
                .overview(request.getOverview())
                .metaScore(request.getMetaScore())
                .director(request.getDirector())
                .star1(request.getStar1())
                .star2(request.getStar2())
                .star3(request.getStar3())
                .star4(request.getStar4())
                .noOfVotes(request.getNoOfVotes())
                .posterLink(posterUrl)
                .status(0)
                .build();

        repository.save(movie);
    }

    public void changeMovieStatus(Long movieId, int newStatus) {
        Movie movie = repository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + movieId));

        movie.setStatus(newStatus);
        repository.save(movie);
    }

    public void updateMovie(MovieDTO req, Long movieId) throws IOException {
        Movie movie = repository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + movieId));

        String posterUrl = req.getPosterLink();
        if ((posterUrl == null || posterUrl.isEmpty()) && req.getPosterImage() != null) {
            posterUrl = storageService.uploadImage(req.getPosterImage());
        }

        if (req.getTitle() != null && !req.getTitle().isEmpty()) {
            movie.setTitle(req.getTitle());
        }
        if (req.getReleasedYear() != null && !req.getReleasedYear().isEmpty()) {
            movie.setReleasedYear(req.getReleasedYear());
        }
        if (req.getRuntime() != null && !req.getRuntime().isEmpty()) {
            movie.setRuntime(req.getRuntime());
        }
        if (req.getGenre() != null && !req.getGenre().isEmpty()) {
            movie.setGenre(req.getGenre());
        }
        if (req.getImdbRating() != null) {
            movie.setImdbRating(req.getImdbRating());
        }
        if (req.getOverview() != null && !req.getOverview().isEmpty()) {
            movie.setOverview(req.getOverview());
        }
        if (req.getMetaScore() != null) {
            movie.setMetaScore(req.getMetaScore());
        }
        if (req.getDirector() != null && !req.getDirector().isEmpty()) {
            movie.setDirector(req.getDirector());
        }
        if (req.getStar1() != null && !req.getStar1().isEmpty()) {
            movie.setStar1(req.getStar1());
        }
        if (req.getStar2() != null && !req.getStar2().isEmpty()) {
            movie.setStar2(req.getStar2());
        }
        if (req.getStar3() != null && !req.getStar3().isEmpty()) {
            movie.setStar3(req.getStar3());
        }
        if (req.getStar4() != null && !req.getStar4().isEmpty()) {
            movie.setStar4(req.getStar4());
        }
        if (req.getNoOfVotes() != null) {
            movie.setNoOfVotes(req.getNoOfVotes());
        }
        if (posterUrl != null && !posterUrl.isEmpty()) {
            movie.setPosterLink(posterUrl);
        }

        repository.save(movie);
    }

    public List<MovieDTO> getMovies(String genre, String releasedYear, Integer status,
                                    String sortBy, String sortDirection,
                                    int page, int size) {

        Specification<Movie> spec = Specification.where((Specification<Movie>) null);

        // 1. Lọc Genre (Chứa)
        if (StringUtils.hasText(genre)) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("genre")), "%" + genre.toLowerCase() + "%"));
        }

        // 2. Lọc Released Year (Chính xác)
        if (StringUtils.hasText(releasedYear)) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("releasedYear"), releasedYear));
        }

        // 3. Lọc Status (Chính xác)
        if (status != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("status"), status));
        }

        // 4. Sắp xếp
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        // 5. Phân trang
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Movie> pageResult = repository.findAll(spec, pageable);

        return pageResult.map(Movie::convertToDTO).getContent();
    }

    public List<MovieDTO> searchMovies(String keyword) {
        return repository.findByTitleContainingIgnoreCaseOrOverviewContainingIgnoreCase(keyword, keyword)
                .stream().map(Movie::convertToDTO).toList();
    }

    public void deleteMovie(Long movieId) {
        if (!repository.existsById(movieId)) {
            throw new ResourceNotFoundException("Movie not found with id: " + movieId);
        }
        repository.deleteById(movieId);
    }
}
