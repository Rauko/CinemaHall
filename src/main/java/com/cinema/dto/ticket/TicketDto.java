package com.cinema.dto.ticket;

import com.cinema.model.enums.ImaxGlassesOption;
import com.cinema.model.enums.TicketStatus;

import java.time.LocalDateTime;

public record TicketDto (
    Long id,
    TicketStatus status,
    double price,

    Long screeningId,
    LocalDateTime startTime,
    int duration,

    String hallName,
    int rowNumber,
    int seatNumber,

    Long movieId,
    String movieTitle,

    LocalDateTime reservedAt,
    LocalDateTime purchaseDate,

    ImaxGlassesOption glassOption
) {

}
