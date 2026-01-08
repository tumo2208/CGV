package com.spring.backend.Models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movies", indexes = {
        @Index(name = "idx_movie_released_year", columnList = "releasedYear"),
        @Index(name = "idx_movie_imdb_rating", columnList = "imdbRating")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String posterLink;
    private String releasedYear;
    private String runtime;
    private String genre;
    private double imdbRating;
    private String overview;
    private int metaScore;
    private String director;
    private String star1;
    private String star2;
    private String star3;
    private String star4;
    private long noOfVotes;

    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Showtime> showtimes = new ArrayList<>();
}
