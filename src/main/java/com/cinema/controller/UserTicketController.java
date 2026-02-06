package com.cinema.controller;

import com.cinema.dto.PaymentRequest;
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
@RequestMapping("/api/tickets")
public class UserTicketController {

    private final TicketService ticketService;

    public UserTicketController(TicketService ticketService, UserService userService) {
        this.ticketService = ticketService;
    }

    // My tickets

    @GetMapping
    public List<Ticket> getMyTickets() {
        return ticketService.getMyTickets();
    }

    @GetMapping("/paid")
    public List<Ticket> getPaidTickets() {
        return ticketService.getMyPaidTickets();
    }

    // Create

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody CreateTicketRequest request,
                                               Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        Ticket ticket = ticketService.createTicket(
                request.getScreeningId(),
                request.getSeatId(),
                request.getGlassesOption()
        );

        return ResponseEntity.ok(ticket);
    }

    // Cancel

    @PostMapping("/{ticketId}/cancel")
    public ResponseEntity<Ticket> cancelTicket(@PathVariable Long ticketId) {
        return ResponseEntity.ok(ticketService.cancelReservation(ticketId));
    }

    // Pay

    @PostMapping("/{ticketId}/pay")
    public ResponseEntity<Ticket> payForTicket(@PathVariable Long ticketId,
                                            @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(ticketService.payForTicket(ticketId, request));
    }
}
