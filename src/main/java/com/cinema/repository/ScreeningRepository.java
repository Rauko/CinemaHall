package com.cinema.repository;

import com.cinema.model.Movie;
import com.cinema.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening,Long> {
    List<Screening> findByMovie(Movie movie);
    List<Screening> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
}
