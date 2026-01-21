package com.spring.backend.Models;

import com.spring.backend.DTOs.Movie.MovieDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private int status;

    public MovieDTO convertToDTO() {
        return MovieDTO.builder()
                .id(this.id)
                .title(this.title)
                .releasedYear(this.releasedYear)
                .runtime(this.runtime)
                .genre(this.genre)
                .imdbRating(this.imdbRating)
                .overview(this.overview)
                .metaScore(this.metaScore)
                .director(this.director)
                .star1(this.star1)
                .star2(this.star2)
                .star3(this.star3)
                .star4(this.star4)
                .noOfVotes(this.noOfVotes)
                .posterLink(this.posterLink)
                .build();
    }
}
