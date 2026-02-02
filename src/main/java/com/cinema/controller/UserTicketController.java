package com.cinema.controller;

import com.cinema.dto.ticket.CreateTicketRequest;
import com.cinema.model.Ticket;
import com.cinema.model.User;
import com.cinema.service.TicketService;
import com.cinema.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserTicketController {

    private final TicketService ticketService;
    private final UserService userService;

    public UserTicketController(TicketService ticketService, UserService userService) {
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @GetMapping("/{userId}/tickets")
    public List<Ticket> getTickets(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ticketService.getTicketsByUser(user);
    }

    @GetMapping("/{userId}/tickets/paid")
    public List<Ticket> getPaidTickets(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ticketService.getPaidTicketsByUser(user);
    }

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody CreateTicketRequest request,
                                               Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        Ticket ticket = ticketService.createTicket(
                user.getId(),
                request.getScreeningId(),
                request.getSeatId(),
                request.getGlassesOption()
        );

        return ResponseEntity.ok(ticket);
    }

    @PostMapping("/{ticketId}/cancel")
    public ResponseEntity<Ticket> cancelTicket(
            @PathVariable Long ticketId,
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(ticketService.cancelReservation(ticketId, userId));
    }
}
