package com.cinema.controller;

import com.cinema.model.Screening;
import com.cinema.service.ScreeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/screenings")
public class ScreeningController {
    private final ScreeningService screeningService;

    @GetMapping
    public List<Screening> getAllScreenings() {
        return screeningService.findAllScreenings();
    }

    @GetMapping("/movie/{movieId}")
    public List<Screening> getScreeningsByMovie(@PathVariable Long movieId) {
        return screeningService.getScreeningsByMovie(movieId);
    }

    @PostMapping
    public ResponseEntity<Screening> createScreening(@RequestBody Map<String, Object> request) {
        Long movieId = Long.valueOf(request.get("movieId").toString());
        String hall = request.get("hallName").toString();
        LocalDateTime date = LocalDateTime.parse(request.get("date").toString());
        int duration = Integer.parseInt(request.get("duration").toString());
        double price = Double.parseDouble(request.get("price").toString());

        Screening screening = screeningService.addScreening(movieId, hall, date, duration, price);
        return ResponseEntity.ok(screening);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScreening(@PathVariable Long id) {
        screeningService.deleteScreening(id);
        return ResponseEntity.noContent().build();
    }
}
