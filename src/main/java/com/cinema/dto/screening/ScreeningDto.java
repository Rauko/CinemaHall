package com.cinema.dto.screening;

import java.time.LocalDateTime;

public record ScreeningDto (
        Long id,
        LocalDateTime startTime,
        int duration,

        Long movieId,
        String title,

        Long hallId,
        String hallName
) {}
