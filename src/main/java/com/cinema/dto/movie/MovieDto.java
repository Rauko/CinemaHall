package com.cinema.dto.movie;

import com.cinema.model.Director;
import com.cinema.model.enums.MovieGenre;

import java.util.Set;

public record MovieDto(
        String title,
        String description,
        Set<MovieGenre> genres,
        Integer duration,
        Integer releaseYear,
        Director director,
        String posterUrl
) {}
