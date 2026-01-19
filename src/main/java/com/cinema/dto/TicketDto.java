package com.cinema.dto;

import com.cinema.model.Ticket;
import com.cinema.model.TicketStatus;

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
    LocalDateTime purchaseDate
) {
    public static TicketDto fromEntity(Ticket ticket){
        return new TicketDto(
                ticket.getId(),
                ticket.getStatus(),
                ticket.getPrice(),

                ticket.getScreening().getId(),
                ticket.getScreening().getStartTime(),
                ticket.getScreening().getDuration(),

                ticket.getScreening().getHallName(),
                ticket.getSeat().getRowNumber(),
                ticket.getSeat().getSeatNumber(),

                ticket.getScreening().getMovie().getId(),
                ticket.getScreening().getMovie().getTitle(),

                ticket.getReservedAt(),
                ticket.getPurchaseDate()
        );
    }
}
