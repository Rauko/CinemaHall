package com.cinema.controller;

import com.cinema.model.enums.TicketStatus;
import com.cinema.repository.MovieRepository;
import com.cinema.repository.TicketRepository;
import com.cinema.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/stats")
public class AdminStatsController {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final TicketRepository ticketRepository;

    public AdminStatsController(UserRepository userRepository,
                                MovieRepository movieRepository,
                                TicketRepository ticketRepository) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.ticketRepository = ticketRepository;
    }

    @GetMapping
    public Map<String, Object> getStats(){
        Map<String, Object> stats = new HashMap<>();
        stats.put("users", userRepository.count());
        stats.put("movies", movieRepository.count());
        stats.put("tickets", ticketRepository.count());
        stats.put("paidTickets", ticketRepository.countByStatus(TicketStatus.PAID));
        return stats;
    }
}
