package com.cinema.controller;

import com.cinema.model.User;
import com.cinema.model.enums.ExportFormat;
import com.cinema.model.enums.TicketStatus;
import com.cinema.repository.MovieRepository;
import com.cinema.repository.TicketRepository;
import com.cinema.repository.UserRepository;
import com.cinema.service.PurchaseHistoryService;
import com.cinema.service.export.PurchaseHistoryExportService;
import com.cinema.util.FilenameConstructorUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/stats")
public class AdminStatsController {

    private static final Logger log = LoggerFactory.getLogger(AdminStatsController.class);

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final TicketRepository ticketRepository;
    private final PurchaseHistoryExportService purchaseHistoryExportService;
    private final PurchaseHistoryService purchaseHistoryService;

    @GetMapping
    public Map<String, Object> getStats(){

        log.info("Admin stats requested");

        Map<String, Object> stats = new HashMap<>();
        stats.put("users", userRepository.count());
        stats.put("movies", movieRepository.count());
        stats.put("tickets", ticketRepository.count());
        stats.put("paidTickets", ticketRepository.countByStatus(TicketStatus.PAID));

        log.info("Admin stats generated: users={}, movies={}, tickets={}, paidTickets={}",
                stats.get("users"),
                stats.get("movies"),
                stats.get("tickets"),
                stats.get("paidTickets")
        );

        return stats;
    }

    @GetMapping("/users/{userId}/history/export")
    public ResponseEntity<byte[]> exportUserHistory(
            @PathVariable Long userId,
            @RequestParam ExportFormat format
    ){
        log.info("Admin export requested: userId={}, format={}", userId, format);

        User user = userRepository.findById(userId).get();

        byte[] file = purchaseHistoryExportService.exportForAdmin(user, format);

        log.info("Admin export completed: userId={}, format={}, fileSizeBytes={}",
                userId,
                format,
                file.length
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + FilenameConstructorUtil.filenameUpToNow(user.getName(), format))
                .body(file);
    }

    @GetMapping("/revenue/day")
    public double revenueForDay(@RequestParam String date){
        log.info("Admin daily revenue requested: date={}", date);

        double revenue = purchaseHistoryService.getRevenueForDay(LocalDate.parse(date));

        log.info("Admin daily revenue calculated: date={}, revenue={}",
                date,
                revenue
        );

        return revenue;
    }

    @GetMapping("/revenue/month")
    public double revenueForMonth(@RequestParam int year,
                                  @RequestParam int month){
        log.info("Admin monthly revenue requested: year={}, month={}",
                year,
                month
        );

        double revenue = purchaseHistoryService.getRevenueForMonth(year, month);

        log.info("Admin monthly revenue calculated: year={}, month={}, revenue={}",
                year,
                month,
                revenue
        );

        return revenue;
    }
}
