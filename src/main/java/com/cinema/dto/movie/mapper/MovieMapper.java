package com.cinema.dto.movie.mapper;

import com.cinema.dto.movie.MovieDto;
import com.cinema.model.Movie;

public class MovieMapper {
    public static MovieDto toDto(Movie movie) {
        return new MovieDto(
                movie.getTitle(),
                movie.getDescription(),
                movie.getGenres(),
                movie.getDuration(),
                movie.getReleaseYear(),
                movie.getDirector(),
                movie.getPosterUrl()
        );
    }
}
