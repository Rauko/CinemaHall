package com.cinema.dto.actor;

import com.cinema.model.Movie;

import java.time.LocalDate;
import java.util.Set;

public record ActorDto (
        Long id,
        String name,
        LocalDate birthDate,
        String biography,
        Set<Movie> movies
) {}
