package com.cinema.controller;

import com.cinema.model.User;
import com.cinema.model.enums.ExportFormat;
import com.cinema.service.UserService;
import com.cinema.service.export.PurchaseHistoryExportService;
import com.cinema.util.DateParseUtil;
import com.cinema.util.FilenameConstructorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/history")
public class AdminPurchaseHistoryController {

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

        String username = user.getId() + "(" +user.getName() + ")";

        byte[] file = purchaseHistoryExportService
                .exportForAdminPeriod(user, startDate, endDate, format);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" +
                                FilenameConstructorUtil.filename(username, startDate, endDate, format))
                .body(file);
    }
}
