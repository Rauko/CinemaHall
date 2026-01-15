package com.cinema.controller;

import com.cinema.model.Ticket;
import com.cinema.model.TicketStatus;
import com.cinema.model.User;
import com.cinema.repository.TicketRepository;
import com.cinema.repository.UserRepository;
import com.cinema.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserRepository userRepository;
    private final TicketService ticketService;

    public AdminUserController(UserRepository userRepository, TicketService ticketService) {
        this.userRepository = userRepository;
        this.ticketService = ticketService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserByID(@PathVariable Long id){
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/tickets")
    public ResponseEntity<List<Ticket>> getUserTickets(@PathVariable Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(ticketService.getTicketsByUser(user));
    }

    @GetMapping("/{id}/tickets/paid")
    public ResponseEntity<List<Ticket>> getUserPaidTickets(@PathVariable Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(ticketService.getTicketsByUserAndStatus(user, TicketStatus.PAID));
    }

    @GetMapping("/{id}/tickets/reserved")
    public ResponseEntity<List<Ticket>> getUserReservedTickets(@PathVariable Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(ticketService.getTicketsByUserAndStatus(user, TicketStatus.RESERVED));
    }
}
