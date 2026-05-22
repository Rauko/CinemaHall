package com.cinema.controller;

import com.cinema.dto.screening.ScreeningDto;
import com.cinema.dto.screening.mapper.ScreeningMapper;
import com.cinema.dto.screening.request.CreateScreeningRequest;
import com.cinema.model.Screening;
import com.cinema.model.User;
import com.cinema.service.ScreeningService;
import com.cinema.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/screenings")
public class AdminScreeningController {

    private static final Logger log =
            LoggerFactory.getLogger(AdminScreeningController.class);

    private final ScreeningService screeningService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<ScreeningDto> createScreening(
            @RequestBody CreateScreeningRequest request){

        User admin = userService.getCurrentUser();

        log.info("ADMIN ACTION by {}({}): Creating screening for movieId={}, hall={}, startTime={}",
                 admin.getName(),
                 admin.getId(),
                 request.getMovieId(),
                 request.getHallName(),
                 request.getStartTime());

        Screening screening = screeningService.addScreening(
                request.getMovieId(),
                request.getHallName(),
                request.getStartTime(),
                request.getDuration(),
                request.getPrice()
        );

        log.info("ADMIN ACTION by {}({}): Screening created. screeningId={}, movieId={}, hall={}",
                 admin.getName(),
                 admin.getId(),
                 screening.getId(),
                 screening.getMovie().getId(),
                 screening.getHallName());

        return ResponseEntity.ok(
                ScreeningMapper.toDto(screening)
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteScreening(@PathVariable Long id){

        User admin = userService.getCurrentUser();

        Screening screening = screeningService.getScreeningById(id);

        log.info("ADMIN ACTION by {}({}): Deleting screeningId={}, movieId={}, hall={}",
                 admin.getName(),
                 admin.getId(),
                 screening.getId(),
                 screening.getMovie().getId(),
                 screening.getHallName());

        log.info("ADMIN ACTION by {}({}): Deleting screeningId={}, movieId={}, hall={}",
                 admin.getName(),
                 admin.getId(),
                 screening.getId(),
                 screening.getMovie().getId(),
                 screening.getHallName());

        screeningService.deleteScreening(id);

        log.info("ADMIN ACTION by {}({}): Screening deleted. screeningId={}",
                 admin.getName(),
                 admin.getId(),
                 id);

        return ResponseEntity.noContent().build();
    }
}
