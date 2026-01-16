package com.spring.backend.DTOs.Movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    private Long id;
    private String title;
    private String releasedYear;
    private String runtime;
    private String genre;
    private Double imdbRating;
    private String overview;
    private Integer metaScore;
    private String director;
    private String star1;
    private String star2;
    private String star3;
    private String star4;
    private Long noOfVotes;

    private String posterLink;
    private MultipartFile posterImage;
}
