package com.cinema.service;

import com.cinema.model.Movie;
import com.cinema.model.Screening;
import com.cinema.repository.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreeningService {
    private final ScreeningRepository screeningRepository;
    private final MovieService movieService;

    public List<Screening> findAllScreenings() {
        return screeningRepository.findAll();
    }

    public List<Screening> getScreeningsByMovie(Long movieId) {
        Movie movie = movieService.getMovieById(movieId);
        return screeningRepository.findByMovie(movie);
    }

    public Screening addScreening(Long movieId,
                                  String hallName,
                                  LocalDateTime startTime,
                                  int duration,
                                  double price) {
        Movie movie = movieService.getMovieById(movieId);

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
