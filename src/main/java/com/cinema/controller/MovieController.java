package com.cinema.controller;

import com.cinema.dto.movie.MovieDto;
import com.cinema.dto.movie.mapper.MovieMapper;
import com.cinema.dto.movie.request.CreateMovieRequest;
import com.cinema.dto.movie.request.UpdateMovieRequest;
import com.cinema.model.Movie;
import com.cinema.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    public List<MovieDto> getAllMovies() {
        return movieService.getAllMovies()
                .stream()
                .map(MovieMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public MovieDto getMovieById(@PathVariable Long id) {
        Movie movie = movieService.getMovieById(id);
        return MovieMapper.toDto(movie);
    }

    @PostMapping("/add")
    public ResponseEntity<MovieDto> addMovie(
            @RequestBody CreateMovieRequest request) {

        Movie movie = movieService.createMovie(request);
        return ResponseEntity.ok(MovieMapper.toDto(movie));
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<MovieDto> updateMovie(
            @PathVariable Long id,
            @RequestBody UpdateMovieRequest request) {
        try {
            Movie updated = movieService.updateMovie(id, request);
            return ResponseEntity.ok(MovieMapper.toDto(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
