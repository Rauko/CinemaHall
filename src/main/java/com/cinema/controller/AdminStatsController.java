package com.cinema.controller;

import com.cinema.model.User;
import com.cinema.model.enums.ExportFormat;
import com.cinema.model.enums.TicketStatus;
import com.cinema.repository.MovieRepository;
import com.cinema.repository.PurchaseHistoryRepository;
import com.cinema.repository.TicketRepository;
import com.cinema.repository.UserRepository;
import com.cinema.service.export.PurchaseHistoryExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/stats")
public class AdminStatsController {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final TicketRepository ticketRepository;
    private final PurchaseHistoryExportService purchaseHistoryExportService;

    public AdminStatsController(UserRepository userRepository,
                                MovieRepository movieRepository,
                                TicketRepository ticketRepository,
                                PurchaseHistoryExportService purchaseHistoryExportService) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.ticketRepository = ticketRepository;
        this.purchaseHistoryExportService = purchaseHistoryExportService;
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

    @GetMapping("/users/{userId}/history/export")
    public ResponseEntity<byte[]> exportUserHistory(
            @PathVariable Long userId,
            @RequestParam ExportFormat format
    ){
        User user = userRepository.findById(userId).get();

        byte[] file = purchaseHistoryExportService.exportForAdmin(user, format);

        String timestamp = java.time.LocalDateTime.now()
                .withSecond(0)
                .withNano(0)
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));

        String filename = "user_" + userId + "_" + timestamp + "." +
                format.name().toLowerCase();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + filename
                )
                .body(file);

    }
}
