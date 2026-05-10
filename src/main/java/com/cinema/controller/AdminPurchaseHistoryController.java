package com.cinema.controller;

import com.cinema.model.User;
import com.cinema.model.enums.ExportFormat;
import com.cinema.service.UserService;
import com.cinema.service.export.PurchaseHistoryExportService;
import com.cinema.util.DateParseUtil;
import com.cinema.util.FilenameConstructorUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/history")
public class AdminPurchaseHistoryController {

    private static final Logger log =
            LoggerFactory.getLogger(AdminPurchaseHistoryController.class);

    private final UserService userService;
    private final PurchaseHistoryExportService purchaseHistoryExportService;

    @GetMapping("/users/{userId}/export/period")
    public ResponseEntity<byte[]> exportUserHistoryForPeriod(
            @PathVariable Long userId,
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam ExportFormat format) {

        LocalDateTime startDate = DateParseUtil.parseStartDate(start);
        LocalDateTime endDate = DateParseUtil.parseEndDate(end);

        User user = userService.getUserById(userId);
        User admin = userService.getCurrentUser();

        log.info(
                "ADMIN ACTION by {}({}): Export history requested for userId={}, format={}, start={}, end={}",
                admin.getName(),
                admin.getId(),
                userId,
                format,
                startDate,
                endDate
        );

        String username = user.getId() + "(" +user.getName() + ")";

        byte[] file = purchaseHistoryExportService
                .exportForAdminPeriod(
                        user,
                        startDate,
                        endDate,
                        format
                );

        log.info(
                "ADMIN ACTION by {}({}): Export completed for userId={}, format={}, fileSizeBytes={}",
                admin.getName(),
                admin.getId(),
                userId,
                format,
                file.length
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" +
                                FilenameConstructorUtil.filename(
                                        username,
                                        startDate,
                                        endDate,
                                        format
                                )
                )
                .body(file);
    }
}
