package com.cinema.service;

import com.cinema.model.Movie;
import com.cinema.model.Screening;
import com.cinema.repository.MovieRepository;
import com.cinema.repository.ScreeningRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScreeningService {
    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;

    public ScreeningService(ScreeningRepository screeningRepository, MovieRepository movieRepository) {
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
    }

    public List<Screening> findAllScreenings() {
        return screeningRepository.findAll();
    }

    public List<Screening> getScreeningsByMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        return screeningRepository.findByMovie(movie);
    }

    public Screening addScreening(Long movieId, String hallName, LocalDateTime startTime, int duration, double price) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Screening screening = new Screening();
        screening.setMovie(movie);
        screening.setHallName(hallName);
        screening.setStartTime(startTime);
        screening.setDuration(duration);
        screening.setBasePrice(price);

        return screeningRepository.save(screening);
    }

    public void deleteScreening(Long id) {
        screeningRepository.deleteById(id);
    }
}
