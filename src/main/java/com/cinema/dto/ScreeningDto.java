package com.cinema.dto;

import com.cinema.model.Screening;

import java.time.LocalDateTime;

public record ScreeningDto (
        Long id,
        LocalDateTime startTime,
        int duration,

        Long movieId,
        String title,

        Long hallId,
        String hallName
) {
    public static ScreeningDto fromEntity(Screening screening) {
        return new ScreeningDto(
                screening.getId(),
                screening.getStartTime(),
                screening.getDuration(),

                screening.getMovie().getId(),
                screening.getMovie().getTitle(),

                screening.getHall().getId(),
                screening.getHall().getName()
        );
    }
}
