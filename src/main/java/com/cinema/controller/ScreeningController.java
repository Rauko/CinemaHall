package com.cinema.controller;

import com.cinema.dto.screening.ScreeningDto;
import com.cinema.dto.screening.mapper.ScreeningMapper;
import com.cinema.dto.screening.request.CreateScreeningRequest;
import com.cinema.model.Screening;
import com.cinema.service.ScreeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/screenings")
public class ScreeningController {
    private final ScreeningService screeningService;

    @GetMapping("/all")
    public List<ScreeningDto> getAllScreenings() {
        return screeningService.findAllScreenings()
                .stream()
                .map(ScreeningMapper::toDto)
                .toList();
    }

    @GetMapping("/movie/{movieId}")
    public List<ScreeningDto> getScreeningsByMovie(@PathVariable Long movieId) {
        return screeningService.getScreeningsByMovie(movieId)
                .stream()
                .map(ScreeningMapper::toDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<ScreeningDto> createScreening(
            @RequestBody CreateScreeningRequest request) {

        Screening screening = screeningService.addScreening(
                request.getMovieId(),
                request.getHallName(),
                request.getStartTime(),
                request.getDuration(),
                request.getPrice());
        return ResponseEntity.ok(ScreeningMapper.toDto(screening));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteScreening(@PathVariable Long id) {
        screeningService.deleteScreening(id);
        return ResponseEntity.noContent().build();
    }
}
