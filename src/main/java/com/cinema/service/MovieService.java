package com.cinema.service;

import com.cinema.dto.movie.request.CreateMovieRequest;
import com.cinema.dto.movie.request.UpdateMovieRequest;
import com.cinema.exception.MovieNotFoundException;
import com.cinema.model.Movie;
import com.cinema.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private static final Logger log = LoggerFactory.getLogger(MovieService.class);

    private final MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        log.info("Fetching all movies");

        List<Movie> movies = movieRepository.findAll();

        log.debug("Movies fetched: count={}", movies.size());

        return movies;
    }

    public Movie getMovieById(Long id) {

        log.debug("Fetching movie by id={}", id);

        return movieRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Movie not found: id={}", id);
                    return new MovieNotFoundException(id);
                });
    }

    public Movie createMovie(CreateMovieRequest request) {

        log.info("Creating movie: title={}, releaseYear={}",
                request.getTitle(),
                request.getReleaseYear()
        );

        Movie movie = new Movie();
        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setGenres(request.getGenres());
        movie.setDuration(request.getDuration());
        movie.setReleaseYear(request.getReleaseYear());
        movie.setDirector(request.getDirector());
        movie.setPosterUrl(request.getPosterUrl());

        movieRepository.save(movie);

        log.info("Movie created: id={}, title={}",
                movie.getId(),
                movie.getTitle()
        );

        return movie;
    }

    public Movie updateMovie(Long id, UpdateMovieRequest request) {

        log.info("Updating movie: id={}", id);

        Movie movie = getMovieById(id);

        if (request.getTitle() != null) movie.setTitle(request.getTitle());
        if (request.getDescription() != null) movie.setDescription(request.getDescription());
        if (request.getGenres() != null) movie.setGenres(request.getGenres());
        if (request.getDuration() != null) movie.setDuration(request.getDuration());
        if (request.getReleaseYear() != null) movie.setReleaseYear(request.getReleaseYear());
        if (request.getDirector() != null) movie.setDirector(request.getDirector());
        if (request.getPosterUrl() != null) movie.setPosterUrl(request.getPosterUrl());

        movieRepository.save(movie);

        log.info("Movie updated: id={}, title={}",
                movie.getId(),
                movie.getTitle()
        );

        return movie;
    }

    public void deleteMovie(Long id) {

        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Movie not found for deletion: id={}", id);
                    return new MovieNotFoundException(id);
                });

        log.info("Deleting movie: id={}, title={}",
                movie.getId(),
                movie.getTitle()
        );

        movieRepository.delete(movie);

        log.info("Movie deleted: id={}, title={}",
                movie.getId(),
                movie.getTitle()
        );
    }
}
