package com.cinema.dto.movie.request;

import com.cinema.model.Director;
import com.cinema.model.enums.MovieGenre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CreateMovieRequest {

    @NotBlank
    private String title;

    private String description;

    private Set<MovieGenre> genres;

    @NotNull
    private Integer duration;

    private Integer releaseYear;

    private Director director;

    private String posterUrl;

}
