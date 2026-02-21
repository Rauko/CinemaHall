package com.cinema.service;

import com.cinema.model.Movie;
import com.cinema.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Optional<Movie> getMovieById(Long id){
        return movieRepository.findById(id);
    }

    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public Movie updateMovie(Long id, Movie updateMovie) {
        return movieRepository.findById(id)
                .map(movie -> {
                    movie.setTitle(updateMovie.getTitle());
                    movie.setDescription(updateMovie.getDescription());
                    movie.setGenres(updateMovie.getGenres());
                    movie.setDuration(updateMovie.getDuration());
                    movie.setDirector(updateMovie.getDirector());
                    movie.setReleaseYear(updateMovie.getReleaseYear());
                    return movieRepository.save(movie);
                })
                .orElseThrow(() -> new RuntimeException("Movie not found"));
    }

    public void deleteMovie(Long id){
        movieRepository.deleteById(id);
    }
}
