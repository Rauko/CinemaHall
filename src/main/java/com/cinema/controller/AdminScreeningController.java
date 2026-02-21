package com.cinema.controller;

import com.cinema.model.Screening;
import com.cinema.service.ScreeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/screenings")
public class AdminScreeningController {

    private final ScreeningService screeningService;

    @PostMapping
    public ResponseEntity<Screening> createScreening(@RequestBody Map<String,Object> request){
        Long movieId = Long.valueOf(request.get("id").toString());
        String hallName = request.get("hallName").toString();
        LocalDateTime startTime = LocalDateTime.parse(request.get("startTime").toString());
        int duration = Integer.parseInt(request.get("duration").toString());
        double basePrice = Double.parseDouble(request.get("basePrice").toString());

        Screening screening = screeningService.addScreening(movieId, hallName, startTime, duration, basePrice);
        return ResponseEntity.ok(screening);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteScreening(@RequestBody Long id){
        screeningService.deleteScreening(id);
        return ResponseEntity.noContent().build();
    }
}
