package com.cinema.service;

import com.cinema.dto.movie.request.CreateMovieRequest;
import com.cinema.dto.movie.request.UpdateMovieRequest;
import com.cinema.exception.MovieNotFoundException;
import com.cinema.model.Movie;
import com.cinema.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(Long id){
        return movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));
    }

    public Movie createMovie(CreateMovieRequest request) {
        Movie movie = new Movie();
        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setGenres(request.getGenres());
        movie.setDuration(request.getDuration());
        movie.setReleaseYear(request.getReleaseYear());
        movie.setDirector(request.getDirector());
        movie.setPosterUrl(request.getPosterUrl());

        return movieRepository.save(movie);
    }

    public Movie updateMovie(Long id, UpdateMovieRequest request) {

        Movie movie = getMovieById(id);

        if (request.getTitle() != null)  movie.setTitle(request.getTitle());
        if (request.getDescription() != null)  movie.setDescription(request.getDescription());
        if (request.getGenres() != null)  movie.setGenres(request.getGenres());
        if (request.getDuration() != null)  movie.setDuration(request.getDuration());
        if (request.getReleaseYear() != null)  movie.setReleaseYear(request.getReleaseYear());
        if (request.getDirector() != null)  movie.setDirector(request.getDirector());
        if (request.getPosterUrl() != null)  movie.setPosterUrl(request.getPosterUrl());

        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id){
        movieRepository.deleteById(id);
    }
}
