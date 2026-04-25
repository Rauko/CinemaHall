package com.cinema.service;

import com.cinema.exception.ScreeningNotFoundException;
import com.cinema.model.Movie;
import com.cinema.model.Screening;
import com.cinema.repository.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreeningService {
    private final ScreeningRepository screeningRepository;
    private final MovieService movieService;
    private static final Logger log = LoggerFactory.getLogger(ScreeningService.class);

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

        log.info("Creating screening: movieId={}, hallName={}, startTime={}, duration={}, price={}",
                movieId, hallName, startTime, duration, price);

        Movie movie;

        try {
            movie = movieService.getMovieById(movieId);
        } catch (Exception e) {
            log.warn("Movie not found for screening: movieId={}", movieId);
            throw e;
        }

        Screening screening = new Screening();
        screening.setMovie(movie);
        screening.setHallName(hallName);
        screening.setStartTime(startTime);
        screening.setDuration(duration);
        screening.setBasePrice(price);

        log.info("Screening created: id={}, movieId={}, hallName={}",
                screening.getId(),
                movieId,
                hallName
        );

        return screeningRepository.save(screening);
    }

    public void deleteScreening(Long id) {
        Screening screening = screeningRepository.findById(id)
                .orElseThrow(() -> new ScreeningNotFoundException(id));

        log.info("Deleting screening: id={}, movieId={}, hallName={}",
                screening.getId(),
                screening.getMovie().getId(),
                screening.getHallName()
        );

        screeningRepository.delete(screening);

        log.info("Screening deleted: id={}", id);
    }
}
