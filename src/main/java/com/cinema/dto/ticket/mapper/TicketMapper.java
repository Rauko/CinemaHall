package com.cinema.dto.ticket.mapper;

import com.cinema.dto.ticket.TicketDto;
import com.cinema.model.Ticket;

public class TicketMapper {
    public static TicketDto toDto(Ticket ticket){
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
                ticket.getPurchaseDate(),

                ticket.getGlassesOption()
        );
    }
}
