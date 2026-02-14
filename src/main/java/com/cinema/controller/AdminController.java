package com.cinema.controller;

import com.cinema.model.enums.ExportFormat;
import com.cinema.service.export.PurchaseHistoryExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final PurchaseHistoryExportService purchaseHistoryExportService;

    public AdminController(PurchaseHistoryExportService purchaseHistoryExportService) {
        this.purchaseHistoryExportService = purchaseHistoryExportService;
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("ADMIN PANEL");
    }

    @GetMapping("/users/history/export")
    public ResponseEntity<byte[]> exportAllUsersHistory(
            @RequestParam ExportFormat format
    ) {
        byte[] file = purchaseHistoryExportService.exportAllUsers(format);

        String timestamp = java.time.LocalDateTime.now()
                .withSecond(0)
                .withNano(0)
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));

        String filename = "all-users-history" + timestamp + "." + format.name().toLowerCase();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + filename)
                .body(file);
    }
}
