package com.spring.backend.Repositories.Movie;

import com.spring.backend.Models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {
    List<Movie> findByTitleContainingIgnoreCaseOrOverviewContainingIgnoreCase(
            String titleKeyword, String overviewKeyword);
}
