package com.cinema.dto.movie;

import com.cinema.model.Movie;
import com.cinema.model.enums.MovieGenre;

import java.util.Set;

public record MovieDto(
        Long id,
        String title,
        String description,
        int year,
        int duration,
        Set<MovieGenre> genres
) {

    public static MovieDto fromEntity(Movie movie) {
        return new MovieDto(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getReleaseYear(),
                movie.getDuration(),
                movie.getGenres()
        );
    }
}
