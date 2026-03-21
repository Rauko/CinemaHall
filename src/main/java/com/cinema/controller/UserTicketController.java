package com.cinema.controller;

import com.cinema.dto.PaymentRequest;
import com.cinema.dto.ticket.TicketDto;
import com.cinema.dto.ticket.mapper.TicketMapper;
import com.cinema.dto.ticket.request.CreateTicketRequest;
import com.cinema.model.Ticket;
import com.cinema.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tickets")
public class UserTicketController {

    private final TicketService ticketService;

    // My tickets
    @GetMapping
    public List<TicketDto> getMyTickets() {
        return ticketService.getMyTickets()
                .stream()
                .map(TicketMapper::toDto)
                .toList();
    }

    @GetMapping("/paid")
    public List<TicketDto> getPaidTickets() {
        return ticketService.getMyPaidTickets()
                .stream()
                .map(TicketMapper::toDto)
                .toList();
    }

    // Create
    @PostMapping
    public ResponseEntity<TicketDto> createTicket(@RequestBody CreateTicketRequest request) {
        Ticket ticket = ticketService.createTicket(
                request.getScreeningId(),
                request.getSeatId(),
                request.getGlassesOption()
        );

        return ResponseEntity.ok(TicketMapper.toDto(ticket));
    }

    // Cancel
    @PostMapping("/{ticketId}/cancel")
    public ResponseEntity<TicketDto> cancelTicket(@PathVariable Long ticketId) {
        return ResponseEntity.ok(
                TicketMapper.toDto(
                        ticketService.cancelReservation(ticketId)
                )
        );
    }

    // Pay
    @PostMapping("/{ticketId}/pay")
    public ResponseEntity<TicketDto> payForTicket(@PathVariable Long ticketId,
                                            @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(
                TicketMapper.toDto(
                        ticketService.payForTicket(ticketId, request)
                )
        );
    }
}
