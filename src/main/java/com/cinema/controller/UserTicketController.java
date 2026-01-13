package com.cinema.controller;

import com.cinema.model.Ticket;
import com.cinema.model.User;
import com.cinema.service.TicketService;
import com.cinema.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
