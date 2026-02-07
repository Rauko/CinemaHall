package com.cinema.controller;

import com.cinema.dto.ticket.TicketDto;
import com.cinema.model.Ticket;
import com.cinema.model.enums.TicketStatus;
import com.cinema.model.User;
import com.cinema.service.TicketService;
import com.cinema.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/me")
public class MeController {

    private final TicketService ticketService;

    public MeController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/tickets")
    public List<TicketDto> myTickets() {
        return ticketService.getMyTickets()
                .stream()
                .map(TicketDto::fromEntity)
                .toList();
    }

    @GetMapping("/tickets/paid")
    public List<TicketDto> myPaidTickets() {
        return ticketService.getMyPaidTickets()
                .stream()
                .map(TicketDto::fromEntity)
                .toList();
    }

    @GetMapping("/tickets/reserved")
    public List<TicketDto> myReservedTickets() {
        return ticketService.getMyTicketsByStatus(TicketStatus.RESERVED)
                .stream()
                .map(TicketDto::fromEntity)
                .toList();
    }
}
