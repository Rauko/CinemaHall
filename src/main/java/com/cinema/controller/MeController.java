package com.cinema.controller;

import com.cinema.dto.TicketDto;
import com.cinema.model.Ticket;
import com.cinema.model.TicketStatus;
import com.cinema.model.User;
import com.cinema.service.TicketService;
import com.cinema.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/me")
public class MeController {

    private final UserService userService;
    private final TicketService ticketService;

    public MeController(UserService userService, TicketService ticketService) {
        this.userService = userService;
        this.ticketService = ticketService;
    }

    @GetMapping("/tickets")
    public List<TicketDto> myTickets() {
        User user = userService.getCurrentUser();
        List<Ticket> tickets = ticketService.getTicketsByUser(user);
        return tickets.stream().map(TicketDto::fromEntity).toList();
    }

    @GetMapping("/tickets/paid")
    public List<TicketDto> myPaidTickets() {
        User user = userService.getCurrentUser();
        List<Ticket> tickets = ticketService.getPaidTicketsByUser(user);
        return tickets.stream().map(TicketDto::fromEntity).toList();
    }

    @GetMapping("/tickets/reserved")
    public List<TicketDto> myReservedTickets() {
        User user = userService.getCurrentUser();
        List<Ticket> tickets = ticketService.getTicketsByUserAndStatus(user, TicketStatus.RESERVED);
        return tickets.stream().map(TicketDto::fromEntity).toList();
    }
}
